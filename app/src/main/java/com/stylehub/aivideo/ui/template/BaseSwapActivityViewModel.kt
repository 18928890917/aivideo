package com.stylehub.aivideo.ui.template

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import androidx.room.concurrent.AtomicBoolean
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.GetImageProgressStateEnum
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ProgressCommonReqModel
import com.stylehub.aivideo.network.model.`in`.GetImageFacesReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.GetImageFacesRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageProgressRespDataModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.utils.DownloadUtil
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.SharedPreferenceUtil
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Create by league at 2025/7/7
 *
 * 基础装数据模型
 *
 */
abstract class BaseSwapActivityViewModel<T : BaseSwapActivityUiData>(uiData: T) : BaseViewModel<T>(uiData) {

    var mutableData = _uiStateData.value
    protected var mTemplate: Template? = null
    protected var successDownloadUrl: String? = null
    protected var isInProgress: Boolean = false

    private var isInDelayProgress = AtomicBoolean(false)
    var delayProgress = 0
        private set
        get() {
            if (field < 0)
                field = 0
            if (field > 100)
                field = 100
            return field
        }
    var delayTargetProgress = 0
        private set
        get() {
            if (field < 0)
                field = 0
            if (field > 100)
                field = 100
            return field
        }

    /**
     * 是否需要获取推荐头像
     */
    abstract fun needRecommendHead(): Boolean;

    /**
     * 点击了swap
     */
    abstract fun onSwapClick();

    /**
     * 点击了swap(带分辨率选择)
     */
    open fun onSwapClick(resolution: String?, credits: Int?) {
        onSwapClick()
    }

    /**
     * 可以上传两张图片的时候跳过第二张图
     */
    open fun onSkipClick() {}

    /**
     * 点击了下载
     */
    abstract fun onDownloadClick();

    /**
     * 点击了删除所选照片
     */
    abstract fun onDeleteSelectedPhoto();

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        if (needRecommendHead()) {
            fetchRecommendHeadList()
        }

        viewModelScope.launch {
            delay(500)
            mutableData.showFirstUploadHintDialog =
                SharedPreferenceUtil.get(PrefKey.NEED_SHOW_FIRST_UPLOAD_HINT, true)?:true
            SharedPreferenceUtil.put(PrefKey.NEED_SHOW_FIRST_UPLOAD_HINT, false)
        }
    }

    open fun setIntentData(data: Template?) {
        mTemplate = data
    }

    override fun onBackPressed(): Boolean {

        if (isInProgress) {
            showWaitingTooLongDialog()
            return true
        }
        return super.onBackPressed()
    }

    fun <TMP: Template>getTemplateModel() : TMP? {
        return mTemplate as TMP?
    }

    fun startDelayProgress(progress: Int, speedMillions: Long = 10, onProgress: (progress: Int) -> Unit = {}) {
        if (delayTargetProgress >= progress)
            return
        delayTargetProgress = progress

        if (!isInDelayProgress.getAndSet(true)) {

            viewModelScope.launch {
                while (true) {
                    if (!isInDelayProgress.get())
                        break
                    if (delayProgress >= 100) {
                        stopDelayProgress()
                        break
                    }

                    if (delayProgress < delayTargetProgress) {
                        delayProgress++
                        onProgress(delayProgress)
                    } else {
                        if (delayProgress >= 100) {
                            onProgress(100)
                        }
                    }
                    delay(speedMillions)
                }
            }
        }
    }

    fun stopDelayProgress() {
        isInDelayProgress.set(false)
        delayProgress = 0
        delayTargetProgress = 0
    }

//    fun getDelayProgress(): Int {
//        return delayProgress
//    }
//
//    fun getDelayTargetProgress(): Int {
//
//    }

    /**
     * 显示上传弹窗
     */
    fun showUploadDialog(progress: Int = 0, hint: String = "") {
        mutableData.showUploadDialog = true
        mutableData.uploadProgress = progress
        mutableData.uploadDialogHint = hint
    }

    /**
     * 隐藏上传弹窗
     */
    fun dismissUploadDialog() {
        mutableData.showUploadDialog = false
    }

    /**
     * 显示选择照片弹窗
     */
    fun showSelectPhotoDialog() {

        if (needRecommendHead()) {
            fetchRecommendHeadList()
        }
        mutableData.showSelectPhotoDialog = true
    }

    /**
     * 隐藏选择照片弹窗
     */
    fun dismissSelectPhotoDialog() {
        mutableData.showSelectPhotoDialog = false
    }

    /**
     * 显示等待太久弹窗
     */
    fun showWaitingTooLongDialog() {
        mutableData.showWaitingTooLongDialog = true
    }

    /**
     * 隐藏等待太久弹窗
     */
    fun dismissWaitingTooLongDialog() {
        mutableData.showWaitingTooLongDialog = false
    }

    /**
     * 隐藏首次上传提示弹窗
     */
    fun dismissFirstUploadHintDialog() {
        mutableData.showFirstUploadHintDialog = false
    }

    /**
     * 设置副标题，没有则不显示
     */
    fun setSubTitle(subTitle: String?) {
        mutableData.subTitle = subTitle
    }

    /**
     * 设置中间的进度内容是否显示
     */
    fun showMiddleProgressContent(leftBitmap: Bitmap? = null, rightBitmap: Bitmap? = null,
                                  progress: Int = 0, hint: String = "") {
        mutableData.showMiddleProgressContent = true
        mutableData.showMiddlePic = false
        mutableData.showMiddleLoading = false
        mutableData.showMiddleFail = false
        updateMiddleProgressContent(leftBitmap, rightBitmap, progress, hint)
    }

    /**
     * 更新中间进度内容
     */
    fun updateMiddleProgressContent(leftBitmap: Bitmap? = mutableData.middleContentLeftHeadBitmap,
                                    rightBitmap: Bitmap? = mutableData.middleContentRightHeadBitmap,
                                    progress: Int = 0,
                                    hint: String = mutableData.middleContentHint) {
        mutableData.middleContentLeftHeadBitmap = leftBitmap
        mutableData.middleContentRightHeadBitmap = rightBitmap
        mutableData.middleContentProgress = progress
        mutableData.middleContentHint = hint
    }

    /**
     * 显示中间失败组件
     */
    fun showMiddleFail() {
        mutableData.showMiddleFail = true
        mutableData.showMiddleProgressContent = false
        mutableData.showMiddlePic = false
        mutableData.showMiddleLoading = false
    }

    /**
     * 显示中间读取中组件
     */
    fun showMiddleLoading() {
        mutableData.showMiddleLoading = true
        mutableData.showMiddlePic = false
        mutableData.showMiddleFail = false
        mutableData.showMiddleProgressContent = false
    }

    /**
     * 显示中间大图
     */
    fun showMiddlePic() {
        mutableData.showMiddleLoading = false
        mutableData.showMiddlePic = true
        mutableData.showMiddleFail = false
        mutableData.showMiddleProgressContent = false
    }

    open fun clearBottomBarWithTemplate() {
        mutableData.bottomBarRightHeadBitmap = null
    }

    fun showBottomBarWithTemplate() {
        mutableData.showBottomBarWithTemplate = true
        mutableData.showBottomBarWithProgressBar = false
        mutableData.showBottomBarWithResolution = false
        mutableData.showBottomBarWithSingleButton = false
    }

    fun showBottomBarWithProgress(progress: Int = 0) {
        mutableData.showBottomBarWithTemplate = false
        mutableData.showBottomBarWithProgressBar = true
        mutableData.showBottomBarWithResolution = false
        mutableData.showBottomBarWithSingleButton = false

        mutableData.bottomBarProgress = progress
    }

    fun showBottomBarWithResolution() {
        mutableData.showBottomBarWithTemplate = false
        mutableData.showBottomBarWithProgressBar = false
        mutableData.showBottomBarWithResolution = true
        mutableData.showBottomBarWithSingleButton = false
    }

    fun showBottomBarWithDownloadButton() {
        showBottomBarWithSingleButton()
        mutableData.showDownloadButton = true
        mutableData.showDownloadButtonDisable = false
        mutableData.showStartMakingButton = false
        mutableData.showUploadPhotoButton = false
    }

    fun showBottomBarWithDownloadButtonDisable() {
        showBottomBarWithSingleButton()
        mutableData.showDownloadButton = false
        mutableData.showDownloadButtonDisable = true
        mutableData.showStartMakingButton = false
        mutableData.showUploadPhotoButton = false
    }

    fun showBottomBarWithStartMakingButton() {
        showBottomBarWithSingleButton()
        mutableData.showDownloadButton = false
        mutableData.showDownloadButtonDisable = false
        mutableData.showStartMakingButton = true
        mutableData.showUploadPhotoButton = false
    }

    fun showBottomBarWithUploadPhotoButton(showSkip: Boolean = false) {
        showBottomBarWithSingleButton()
        mutableData.showDownloadButton = false
        mutableData.showDownloadButtonDisable = false
        mutableData.showStartMakingButton = false
        mutableData.showUploadPhotoButton = true
        mutableData.showSkipRightOfUploadPhotoButton = showSkip
    }

    private fun showBottomBarWithSingleButton() {
        mutableData.showBottomBarWithTemplate = false
        mutableData.showBottomBarWithProgressBar = false
        mutableData.showBottomBarWithResolution = false
        mutableData.showBottomBarWithSingleButton = true
    }

    private fun generateTimestamp(): String {
        val currentDate = Date()
        val formatter = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
        return formatter.format(currentDate)
    }

    fun download(url: String?, isVideo: Boolean = false) {
        if (url == null)
            return
        DownloadUtil.downloadFile(mActivity!!, url, "${generateTimestamp()}.${if (isVideo) "mp4" else "jpg"}")
    }

    /**
     * 从相册选中了图片或视频
     */
    abstract fun onSelectPhoto(uri: Uri?);

    /**
     * 选中了推荐头像
     */
    abstract fun onRecommendHeadSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo);

    ///////////////////////////////////////  接口   //////////////////////////////////////////
    /**
     * 获取推荐头像列表
     */
    protected fun fetchRecommendHeadList() {

        if((mutableData.recommendData?.sys?.size ?: 0) > 0) {
            return
        }

        val userId = LoginManager.getUserId() ?: return
        val api = Network()
            .createApi(ApiService::class.java)
        api.getRecommendHeadList(userId).enqueue(object :
            Callback<CommonRespModel<RecommendHeadListRespDataModel>> {
            override fun onResponse(
                call: retrofit2.Call<CommonRespModel<RecommendHeadListRespDataModel>>,
                response: retrofit2.Response<CommonRespModel<RecommendHeadListRespDataModel>>
            ) {
                val resp = response.body()
                val data = resp?.data?.value
                if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {
                    mutableData.recommendData = data
                }
            }

            override fun onFailure(
                call: retrofit2.Call<CommonRespModel<RecommendHeadListRespDataModel>>,
                t: Throwable
            ) {
                // 可选：处理异常
            }
        })
    }

    open fun onProgress(isFinished: Boolean, isFail: Boolean, progress: Int) {

    }

    fun getTaskProgressLoop(taskId: Long?) {

        //可能已经扣款了，刷新下
        LoginManager.updateUserAccount()
        viewModelScope.launch {

            isInProgress = true
            var isFinished = false
            var ttl = 0
            var progress = 0
            while (true) {

                if (ttl >= 10) {
                    onProgress(false, true, progress)
                    break
                }

                val progressResult = getTaskProgress(taskId)
                if (progressResult == null) {
                    ttl++
                    delay(500)
                    continue
                }

                when(progressResult.state) {

                    GetImageProgressStateEnum.Finished.code -> {
                        //完成
                        isFinished = true
                        progress = 100
                        successDownloadUrl = progressResult.imageInfos!![0].imgUrl!!
                        onProgress(true, false, 100)
                        break
                    }
                    GetImageProgressStateEnum.InTheQueue.code -> {
                        onProgress(isFinished, false, progress)
                    }
                    GetImageProgressStateEnum.Making.code -> {
                        progress = progressResult.progress?: 0
                        onProgress(false, false, progress)
                        ttl = 0
                    }
                    else -> {
                        ttl++
                    }
                }
                delay(500)
            }

            isInProgress = false
            if (!isFinished) {
                onProgress(false, true, progress)
            }
        }
    }

    /**
     * 获取生图任务进度
     */
    private suspend fun getTaskProgress(taskId: Long?) : GetImageProgressRespDataModel? {

        if (taskId == null) return null
        val userId = LoginManager.getUserId() ?: return null

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            //调用获取进度的接口
            api.getImageProgress(userId, taskId).enqueue(object : Callback<CommonRespModel<GetImageProgressRespDataModel>> {
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

    /**
     * 获取脸部信息
     */
    suspend fun getFaceInfo(
        srcImgBase64: String,
        progressListener: (bytesWritten: Long, contentLength: Long, done: Boolean) -> Unit = {_,_,_->}
    ) : GetImageFacesRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            val reqData = GetImageFacesReqDataModel(srcImgBase64)
            val commonReq = ProgressCommonReqModel(reqData, progressListener)

            api.getImageFaces(commonReq, userId)
                .enqueue(object : Callback<CommonRespModel<GetImageFacesRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<GetImageFacesRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<GetImageFacesRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            it.resume(resp.data?.value)
                            return
                        }
                        it.resume(null)
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<GetImageFacesRespDataModel>>,
                        t: Throwable
                    ) {
                        it.resume(null)
                    }
                })
        }
    }

}

open class BaseSwapActivityUiData {

    /**
     * 标题文本
     */
    var title by mutableStateOf("Template")

    /**
     * 标题下方文字文本,空则不显示
     */
    var subTitle by mutableStateOf<String?>(null)

    /////////////////////////// 中间大图区域 /////////////////////////////////

    /**
     * 是否显示中间大图
     */
    var showMiddlePic by mutableStateOf(true)

    /**
     * 大图的bitmap
     */
    var largeImageBitmap by mutableStateOf<Bitmap?>(null)

    /**
     * 大图的url
     */
    var largeImageUrl by mutableStateOf<String?>(null)

    /**
     * 大图位置视频url
     */
    var largeVideoUrl by mutableStateOf<String?>(null)

    /**
     * 成功视频url
     */
    var successVideoUrl by mutableStateOf<String?>(null)


    //////////////////////////// 上传进度弹窗控制 //////////////////////////////////

    /**
     * 是否显示上传进度弹窗
     */
    var showUploadDialog by mutableStateOf(false)

    /**
     * 上传进度弹窗提示文案
     */
    var uploadDialogHint by mutableStateOf("")

    /**
     * 上传进度
     */
    var uploadProgress by mutableIntStateOf(0)

    /////////////////////////// 照片选择弹窗控制 /////////////////////////////////////

    /**
     * 推荐头像列表
     */
    var recommendData by mutableStateOf<RecommendHeadListRespDataModel?>(null)

    /**
     * 是否显示选择照片弹窗
     */
    var showSelectPhotoDialog by mutableStateOf(false)


    /////////////////////////// 后台运行弹窗控制 /////////////////////////////////////

    /**
     * 是否显示等待太久后台运行弹窗
     */
    var showWaitingTooLongDialog by mutableStateOf<Boolean>(false)

    /////////////////////////// 首次上传提示弹窗控制 /////////////////////////////////////

    /**
     * 是否显示首次上传提示弹窗
     */
    var showFirstUploadHintDialog by mutableStateOf<Boolean>(false)


    ////////////////////////// 在大图中间显示的合成中进度内容 ///////////////////////
    /**
     * 是否显示上传进度弹窗
     */
    var showMiddleProgressContent by mutableStateOf(false)

    /**
     * 上传进度左侧头像
     */
    var middleContentLeftHeadBitmap by mutableStateOf<Bitmap?>(null)

    /**
     * 上传进度右侧头像
     */
    var middleContentRightHeadBitmap by mutableStateOf<Bitmap?>(null)

    /**
     * 合成进度
     */
    var middleContentProgress by mutableIntStateOf(0)

    /**
     * 合成提示文案
     */
    var middleContentHint by mutableStateOf("")


    /////////////////////////// 中间的合成失败以及读取中内容显示控制 ///////////////////////

    /**
     * 是否显示中间的失败
     */
    var showMiddleFail by mutableStateOf(false)

    /**
     * 是否显示中间的读取中
     */
    var showMiddleLoading by mutableStateOf(false)


    ////////////////////////// 底部区域 ////////////////////////////////////////

    /**
     * 1.显示带模板的底部栏
     */
    var showBottomBarWithTemplate by mutableStateOf(true)

    /**
     * 是否显示点数
     */
    var showCredits by mutableStateOf(true)

    /**
     * 所需点数
     */
    var credits by mutableIntStateOf(1)

    /**
     * 底部条左侧头像
     */
    var bottomBarLeftHeadBitmap by mutableStateOf<Bitmap?>(null)

    /**
     * 底部条右侧头像
     */
    var bottomBarRightHeadBitmap by mutableStateOf<Bitmap?>(null)



    /**
     * 2.是否仅显示单按钮底部
     */
    var showBottomBarWithSingleButton by mutableStateOf(false)

    /**
     * 是否显示上传照片按钮
     */
    var showUploadPhotoButton by mutableStateOf(false)

    /**
     * 是否显示上传照片按钮右边的跳过
     */
    var showSkipRightOfUploadPhotoButton by mutableStateOf(false)

    /**
     * 是否显示开始制作按钮
     */
    var showStartMakingButton by mutableStateOf(false)

    /**
     * 是否显示下载按钮
     */
    var showDownloadButton by mutableStateOf(false)

    /**
     * 是否显示下载按钮（不可点击）
     */
    var showDownloadButtonDisable by mutableStateOf(false)



    /**
     * 3.是否仅显示进度条底部
     */
    var showBottomBarWithProgressBar by mutableStateOf(false)

    /**
     * 底部进度条进度
     */
    var bottomBarProgress by mutableIntStateOf(0)



    /**
     * 3.是否仅显示带分辨率选择的底部
     */
    var showBottomBarWithResolution by mutableStateOf(false)

    /**
     * 帧率-价格字典
     */
    var resolutionMap by mutableStateOf<Map<String, Int>?>(null)


}