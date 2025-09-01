package com.stylehub.aivideo.ui.mine

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.MyTaskImgTypeEnum
import com.stylehub.aivideo.constants.MyTaskTypeEnum
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.MyTaskRecord
import com.stylehub.aivideo.network.model.out.MyTasksRespDataModel
import com.stylehub.aivideo.utils.DownloadUtil
import com.stylehub.aivideo.utils.LoginManager
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

class MineActivityUiData {

    val Tabs = listOf("Swap", "Other")

    var avatarUrl by mutableStateOf<String?>(null)
    var userName by mutableStateOf("Guest")
    var credits by mutableIntStateOf(0)

    var currentSelectIndex by mutableIntStateOf(0)
    var swappedTaskLoading by mutableStateOf(false)
    var swappedTaskList by mutableStateOf<List<MyTaskRecord>>(emptyList())

    var danceTaskLoading by mutableStateOf(false)
    var danceTaskList by mutableStateOf<List<MyTaskRecord>>(emptyList())

    var showPreviewDialog by mutableStateOf(false)
}

class MineActivityViewModel(initialValue: MineActivityUiData = MineActivityUiData()) : BaseViewModel<MineActivityUiData>(initialValue) {

    private val mutableData = _uiStateData.value
    private val SwappedTaskType = "${MyTaskTypeEnum.ImageFaceSwap.code},${MyTaskTypeEnum.ClothesSwap.code},${MyTaskTypeEnum.VideoFaceSwap.code}"
    private val OtherTaskType = "${MyTaskTypeEnum.ClayStyle.code},${MyTaskTypeEnum.DanceVideo.code},${MyTaskTypeEnum.AdvanceFaceSwap.code}"

    private var swappedTaskPage = 1;
    private var otherTaskPage = 1;
    var currentPreviewModel: MyTaskRecord? = null

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

    fun showPreviewDialog(model: MyTaskRecord) {
        currentPreviewModel = model
        mutableData.showPreviewDialog = true
    }

    fun dismissPreviewDialog() {
        currentPreviewModel = null
        mutableData.showPreviewDialog = false
    }

    fun getCurrentPreviewUrl(model: MyTaskRecord? = currentPreviewModel): String? {
        return try {
            model?.imgList?.get(0)?.imgUrl
        } catch (e:Exception) {
            null
        }
    }

    fun getCurrentPreviewType(model: MyTaskRecord? = currentPreviewModel): Int {
        return try {
            model?.imgList?.get(0)?.imgType?: MyTaskImgTypeEnum.Image.code
        } catch (e:Exception) {
            MyTaskImgTypeEnum.Image.code
        }
    }

    private fun generateTimestamp(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun download(model: MyTaskRecord? = currentPreviewModel) {

        model?.let {
            DownloadUtil.downloadFile(mActivity!!,
                getCurrentPreviewUrl(model)!!,
                "${generateTimestamp()}.${if (getCurrentPreviewType() == MyTaskImgTypeEnum.Video.code) "mp4" else "jpg"}")
        }
    }

    fun loadSwappedTask() {

        viewModelScope.launch {

            if (mutableData.swappedTaskLoading)
                return@launch

            mutableData.swappedTaskLoading = true
            val taskRecord = getMyTask(swappedTaskPage, SwappedTaskType)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {
                mutableData.swappedTaskList = taskRecord.records
                swappedTaskPage++
            }
            mutableData.swappedTaskLoading = false
        }
    }

    fun loadOtherTask() {

        viewModelScope.launch {
            if (mutableData.danceTaskLoading)
                return@launch

            mutableData.danceTaskLoading = true
            val taskRecord = getMyTask(otherTaskPage, OtherTaskType)
            if (taskRecord?.records != null && taskRecord.records.isNotEmpty()) {
                mutableData.danceTaskList = taskRecord.records
                otherTaskPage++
            }
            mutableData.danceTaskLoading = false
        }
    }

    private suspend fun getMyTask(page: Int = 1, taskType: String): MyTasksRespDataModel? {

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            api.myTasks(page = page, taskType = taskType).enqueue(object : Callback<CommonRespModel<MyTasksRespDataModel>> {
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
}