package com.stylehub.aivideo.ui.mine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.MyTaskImgTypeEnum
import com.stylehub.aivideo.constants.MyTaskTypeEnum
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.GetImageProgressRespDataModel
import com.stylehub.aivideo.network.model.out.MyTaskRecord
import com.stylehub.aivideo.network.model.out.MyTasksRespDataModel
import com.stylehub.aivideo.utils.DownloadUtil
import com.stylehub.aivideo.utils.LoginManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Create by league at 2025/7/1
 *
 */
enum class TaskState {
    FINISHED,
    FAILED,
    PENDING
    ;

    companion object {

        @JvmStatic
        fun ofName(name: String?): TaskState? {

            for (e: TaskState in TaskState.entries) {
                if (e.name.equals(name, true)) {
                    return e
                }
            }
            return null
        }
    }
}

class TaskListData(
    var taskId: Long? = 0,
    var taskType: Int? = 0,
    var taskState: TaskState? = null,
    var progress: Int? = 0,
    var imgType: Int? = null, //1-图片 2-视频
    var imgUrl: String? = null
) {
    companion object {

        @JvmStatic
        fun build(record: MyTaskRecord): TaskListData {
            return TaskListData(
                record.taskId,
                record.taskType,
                TaskState.ofName(record.state),
                0,
                record.imgList?.elementAtOrNull(0)?.imgType,
                record.imgList?.elementAtOrNull(0)?.imgUrl
            )
        }

        @JvmStatic
        fun buildList(records: List<MyTaskRecord>): List<TaskListData> {
            return records.map { record -> build(record) }
        }
    }

    override fun equals(other: Any?): Boolean {

        if (other == null)
            return false
        if (other !is TaskListData) {
            return false
        }
        return taskId == other.taskId
    }

    override fun hashCode(): Int {
        return taskId?.hashCode() ?: 0
    }
}

// Repository
class TaskRepository {
    private val _activeTaskIds = MutableStateFlow<List<Long>>(emptyList())
    val needUpdateProgressTaskIdList: StateFlow<List<Long>> = _activeTaskIds.asStateFlow()

    fun addTask(taskId: Long) {
        if (_activeTaskIds.value.contains(taskId))
            return
        _activeTaskIds.value += taskId
    }

    fun removeTask(taskId: Long) {
        _activeTaskIds.value -= taskId
    }
}

class MineActivityUiData {

    val Tabs = listOf("Swap", "Other")

    var avatarUrl by mutableStateOf<String?>(null)
    var userName by mutableStateOf("Guest")
    var credits by mutableIntStateOf(0)

    var swappedTaskLoadLatestLoading by mutableStateOf(false)
    var danceTaskLoadLatestLoading by mutableStateOf(false)

    var currentSelectIndex by mutableIntStateOf(0)
    var swappedTaskLoading by mutableStateOf(false)
    var swappedTaskList = mutableStateListOf<TaskListData>()
    var swappedTaskHasMore by mutableStateOf(true)

    var danceTaskLoading by mutableStateOf(false)
    var danceTaskList = mutableStateListOf<TaskListData>()
    var danceTaskHasMore by mutableStateOf(true)

    var showPreviewDialog by mutableStateOf(false)
}

class MineActivityViewModel(initialValue: MineActivityUiData = MineActivityUiData()) :
    BaseViewModel<MineActivityUiData>(initialValue) {

    private val mutableData = _uiStateData.value
    private val SwappedTaskType =
        "${MyTaskTypeEnum.ImageFaceSwap.code},${MyTaskTypeEnum.ClothesSwap.code},${MyTaskTypeEnum.VideoFaceSwap.code}"

    //    private val OtherTaskType =
//        "${MyTaskTypeEnum.ClayStyle.code},${MyTaskTypeEnum.DanceVideo.code},${MyTaskTypeEnum.AdvanceFaceSwap.code}," +
//                "${MyTaskTypeEnum.FpDance.code},${MyTaskTypeEnum.CombinePicTask.code},${MyTaskTypeEnum.I2VCustomTask.code}," +
//                "${MyTaskTypeEnum.DollGenTask.code},${MyTaskTypeEnum.CartoonStyleTask.code},${MyTaskTypeEnum.ClayAndCartoonStyleTask.code}"
    private val OtherTaskType = (listOf(1, 4) + (6..99).toList()).joinToString(",")

    private val taskRepository: TaskRepository = TaskRepository()

    private var swappedTaskPage = 1;
    private var otherTaskPage = 1;
    var currentPreviewModel: TaskListData? = null

    init {
        viewModelScope.launch {
            //更新未完成任务的进度
            updateUnFinishedTaskProgress()
        }
    }

    private fun update() {
        mutableData.avatarUrl = LoginManager.getAvatar()
        mutableData.userName = LoginManager.getUserName() ?: "Guest"
        mutableData.credits = LoginManager.getCredit() ?: 0
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        update()
        loadSwappedTask()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        update()
    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        update()
        loadSwappedTask()
    }

    fun switchTab(index: Int) {
        mutableData.currentSelectIndex = index

        if (index == 0) {
            if (!mutableData.swappedTaskLoading && mutableData.swappedTaskList.isEmpty()) {
                loadSwappedTask()
            }
        } else {
            if (!mutableData.danceTaskLoading && mutableData.danceTaskList.isEmpty()) {
                loadOtherTask()
            }
        }
    }

    private suspend fun updateUnFinishedTaskProgress() {

        //当任务列表更新的时候会取消之前的任务，然后用新列表重新执行里面的任务
        taskRepository.needUpdateProgressTaskIdList.collectLatest { taskIdList ->
            //使用supervisorScope或coroutineScope可以并发执行任务，但supervisorScope每个任务之间不会相互影响，
            //coroutineScope一旦有一个任务失败了，就结束整个协程
            supervisorScope {
                taskIdList.forEach { taskId ->
                    launch {
                        getProgressAndUpdate(taskId)
                    }
                }
            }
        }
    }

    private suspend fun getProgressAndUpdate(taskId: Long) {

        while (true) {

            var list = mutableData.swappedTaskList
            var model: TaskListData? =
                mutableData.swappedTaskList.filter { it.taskId == taskId }.getOrNull(0)
            if (model == null) {
                list = mutableData.danceTaskList
                model = mutableData.danceTaskList.filter { it.taskId == taskId }.getOrNull(0)
            }

            model?.let {
                val progress: GetImageProgressRespDataModel? = getTaskProgress(taskId)
                progress?.run {

                    when (progress.state) {
                        1, 2 -> {
                            model.progress = progress.progress
                        }

                        3 -> {
                            val imgUrl = progress.imageInfos?.get(0)?.imgUrl
                            val isImage = imgUrl?.contains("/image/", true) ?: true
                            model.taskState = TaskState.FINISHED
                            model.imgType = if (isImage) 1 else 2
                            model.progress = progress.progress
                            model.imgUrl = imgUrl
                            taskRepository.removeTask(taskId)
                        }

                        else -> {
                            model.taskState = TaskState.FAILED
                            taskRepository.removeTask(taskId)
                        }
                    }
                    val index = list.indexOf(model)
                    if (index >= 0) {
                        list.removeAt(index)
                        list.add(index, model)
                    }
                }
            }

            delay(500)
        }
    }

    fun showPreviewDialog(model: TaskListData) {

        currentPreviewModel = model
        mutableData.showPreviewDialog = true

    }

    fun dismissPreviewDialog() {
        currentPreviewModel = null
        mutableData.showPreviewDialog = false
    }

    fun getCurrentPreviewUrl(model: TaskListData? = currentPreviewModel): String? {
        return try {
            model?.imgUrl
        } catch (e: Exception) {
            null
        }
    }

    fun getCurrentPreviewType(model: TaskListData? = currentPreviewModel): Int {
        return try {
            model?.imgType ?: MyTaskImgTypeEnum.Image.code
        } catch (e: Exception) {
            MyTaskImgTypeEnum.Image.code
        }
    }

    private fun generateTimestamp(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun download(model: TaskListData? = currentPreviewModel) {

        model?.let {
            DownloadUtil.downloadFile(
                mActivity!!,
                getCurrentPreviewUrl(model)!!,
                "${generateTimestamp()}.${if (getCurrentPreviewType() == MyTaskImgTypeEnum.Video.code) "mp4" else "jpg"}"
            )
        }
    }

    fun loadLatestSwappedTask() {

        if (swappedTaskPage == 1 && mutableData.swappedTaskHasMore)
            return

        viewModelScope.launch {

            if (mutableData.swappedTaskLoadLatestLoading)
                return@launch

            mutableData.swappedTaskLoadLatestLoading = true
            val taskRecord = getMyTask(SwappedTaskType, 1, 3)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {

                val list = TaskListData.buildList(taskRecord.records)
                val notIncludeList = list.filter { !mutableData.swappedTaskList.contains(it) }
                notIncludeList.onEach {
                    if (it.taskId != null && it.taskState == TaskState.PENDING) {
                        taskRepository.addTask(it.taskId!!)
                    }
                }
                mutableData.swappedTaskList.addAll(0, notIncludeList)
            }
            mutableData.swappedTaskLoadLatestLoading = false
        }
    }

    fun loadLatestDanceTask() {

        if (otherTaskPage == 1 && mutableData.danceTaskHasMore)
            return

        viewModelScope.launch {

            if (mutableData.danceTaskLoadLatestLoading)
                return@launch

            mutableData.danceTaskLoadLatestLoading = true
            val taskRecord = getMyTask(OtherTaskType, 1, 3)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {

                val list = TaskListData.buildList(taskRecord.records)
                val notIncludeList = list.filter { !mutableData.danceTaskList.contains(it) }
                notIncludeList.onEach {
                    if (it.taskId != null && it.taskState == TaskState.PENDING) {
                        taskRepository.addTask(it.taskId!!)
                    }
                }
                mutableData.danceTaskList.addAll(0, notIncludeList)
            }
            mutableData.danceTaskLoadLatestLoading = false
        }
    }

    fun loadSwappedTask() {

        if (!mutableData.swappedTaskHasMore)
            return

        viewModelScope.launch {

            if (mutableData.swappedTaskLoading)
                return@launch

            mutableData.swappedTaskLoading = true
            val taskRecord = getMyTask(SwappedTaskType, swappedTaskPage)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {
                mutableData.swappedTaskList.addAll(
                    TaskListData.buildList(taskRecord.records).onEach {
                        if (it.taskId != null && it.taskState == TaskState.PENDING) {
                            taskRepository.addTask(it.taskId!!)
                        }
                    })
                swappedTaskPage++
            }
            mutableData.swappedTaskHasMore = taskRecord?.hasNext ?: true
            mutableData.swappedTaskLoading = false
        }
    }

    fun loadOtherTask() {

        if (!mutableData.danceTaskHasMore)
            return

        viewModelScope.launch {
            if (mutableData.danceTaskLoading)
                return@launch

            mutableData.danceTaskLoading = true
            val taskRecord = getMyTask(OtherTaskType, otherTaskPage)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {
                mutableData.danceTaskList.addAll(TaskListData.buildList(taskRecord.records).onEach {
                    if (it.taskId != null && it.taskState == TaskState.PENDING) {
                        taskRepository.addTask(it.taskId!!)
                    }
                })
                otherTaskPage++
            }
            mutableData.danceTaskHasMore = taskRecord?.hasNext ?: true
            mutableData.danceTaskLoading = false
        }
    }

    private suspend fun getMyTask(
        taskType: String,
        page: Int = 1,
        pageSize: Int = 20
    ): MyTasksRespDataModel? {

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            api.myTasks(page = page, size = pageSize, taskType = taskType)
                .enqueue(object : Callback<CommonRespModel<MyTasksRespDataModel>> {
                    override fun onResponse(
                        call: Call<CommonRespModel<MyTasksRespDataModel>>,
                        response: Response<CommonRespModel<MyTasksRespDataModel>>
                    ) {
                        if (response.isSuccessful && response.body()?.code == 0) {
                            it.resume(response.body()?.data?.value)
                            return
                        }
                        it.resume(null)
                    }

                    override fun onFailure(
                        call: Call<CommonRespModel<MyTasksRespDataModel>>,
                        t: Throwable
                    ) {
                        it.resume(null)
                    }

                })
        }

    }

    /**
     * 获取生图任务进度
     */
    private suspend fun getTaskProgress(taskId: Long?): GetImageProgressRespDataModel? {

        if (taskId == null) return null
        val userId = LoginManager.getUserId() ?: return null

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            //调用获取进度的接口
            api.getImageProgress(userId, taskId)
                .enqueue(object : Callback<CommonRespModel<GetImageProgressRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<GetImageProgressRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<GetImageProgressRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            it.resume(resp.data?.value)
                            return
                        }
                        it.resume(null)
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<GetImageProgressRespDataModel>>,
                        t: Throwable
                    ) {
                        it.resume(null)
                    }

                })
        }
    }
}