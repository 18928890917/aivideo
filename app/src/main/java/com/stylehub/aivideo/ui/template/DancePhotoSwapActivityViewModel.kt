package com.stylehub.aivideo.ui.template

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.stylehub.aivideo.constants.GetImageProgressStateEnum
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.Img2VideoPoseTaskReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.GetImg2VideoPoseTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.Img2VideoPoseTaskRespDataModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.utils.ImageUtil
import com.stylehub.aivideo.utils.LoginManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okhttp3.MediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.ForwardingSink
import okio.Okio
import okio.Sink
import okio.buffer

/**
 *
 * Create by league at 2025/7/2
 *
 */

class DancePhotoSwapActivityData : BaseSwapActivityUiData() {

}

class DancePhotoSwapActivityViewModel(initialValue: DancePhotoSwapActivityData = DancePhotoSwapActivityData()) :
    BaseSwapActivityViewModel<DancePhotoSwapActivityData>(initialValue) {

    private var templateData: GetImg2VideoPoseTemplateRespDataModel? = null

    private var img2VideoPoseReq: Img2VideoPoseTaskReqDataModel? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        setSubTitle("Template")
        showBottomBarWithUploadPhotoButton()
    }

    fun setIntentData(data: GetImg2VideoPoseTemplateRespDataModel?) {

        data?.apply {
            templateData = this
            mutableData.credits = getTmpCredits()

            if (templateUrl != null) {
                mutableData.largeVideoUrl = getTmpUrl()
            } else {
                mutableData.largeImageUrl = getPreviewUrl()
            }
        }
    }

    override fun onSwapClick() {

        //没有选择图片或模板
        if (img2VideoPoseReq == null) {
            showMessageHintDialog("Please upload a photo")
            return
        }

        showBottomBarWithProgress(0)
        viewModelScope.launch {

            val taskResult : Img2VideoPoseTaskRespDataModel? = img2VideoPose()
            img2VideoPoseReq = null

            if (taskResult == null) {
                showMiddleFail()
                return@launch
            }
            val taskId = taskResult.taskId
            getTaskProgressLoop(taskId)
        }
    }

    override fun onProgress(isFinished: Boolean, isFail: Boolean, progress: Int) {
        if (isFinished) {
            viewModelScope.launch {
                showMiddleLoading()
                showBottomBarWithDownloadButton()
                val successBitmap = ImageUtil.getBitmap(successDownloadUrl!!.toUri())
                mutableData.largeImageBitmap = successBitmap
                showMiddlePic()
            }
        } else if (isFail) {
            showMiddleFail()
            mutableData.largeImageBitmap = null
        } else {
            showBottomBarWithProgress(progress = progress)
        }
    }

    override fun onDownloadClick() {
        if (successDownloadUrl != null) {
            download(successDownloadUrl, true)
        }
    }

    override fun onSelectPhoto(uri: Uri?) {

        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val base64Str = ImageUtil.bitmapImgBase64(bitmap)
            mutableData.largeImageBitmap = bitmap
            showBottomBarWithStartMakingButton()

            img2VideoPoseReq = Img2VideoPoseTaskReqDataModel().apply {
                srcImgBase64 = base64Str
            }
        }
    }

    override fun onBackPressed(): Boolean {

        if (isInProgress) {
            showWaitingTooLongDialog()
            return true
        }

        if (img2VideoPoseReq != null) {
            img2VideoPoseReq = null
            mutableData.largeImageBitmap = null
            showBottomBarWithUploadPhotoButton()
            showMiddlePic()
            return true
        }
        return false
    }

    private suspend fun img2VideoPose() : Img2VideoPoseTaskRespDataModel? {
        val userId = LoginManager.getUserId() ?: return null
        val reqModel = img2VideoPoseReq ?: return null
        val templateName = templateData?.templateName ?: return null
        val api = Network().createApi(ApiService::class.java)

        val srcImgBase64Body = reqModel.srcImgBase64?.let {
//            val origin = RequestBody.create("text/plain".toMediaTypeOrNull(), it)
            val origin = it.toRequestBody("text/plain".toMediaTypeOrNull())
            ProgressRequestBody(origin) { bytesWritten, totalBytes ->
                val percent = (bytesWritten * 100 / totalBytes).toInt()
                // 上传中，显示进度
                showUploadDialog(progress = percent, hint = "uploading...")
                if (bytesWritten == totalBytes) {
                    // 上传完成，关闭弹窗
                    dismissUploadDialog()
                }
            }
        }
        // 目前srcVideo为空，如有需要可同理处理
        return suspendCoroutine {
            api.img2VideoPoseTask(
                userId = userId,
                srcImgBase64 = srcImgBase64Body,
                templateName = templateName
            ).enqueue(object : retrofit2.Callback<CommonRespModel<Img2VideoPoseTaskRespDataModel>> {
                override fun onResponse(
                    call: retrofit2.Call<CommonRespModel<Img2VideoPoseTaskRespDataModel>>,
                    response: retrofit2.Response<CommonRespModel<Img2VideoPoseTaskRespDataModel>>
                ) {
                    val resp = response.body()
                    if (response.isSuccessful && resp != null && resp.code == 0) {
                        it.resume(resp.data?.value)
                        return
                    }
                    it.resume(null)
                }
                override fun onFailure(
                    call: retrofit2.Call<CommonRespModel<Img2VideoPoseTaskRespDataModel>>,
                    t: Throwable
                ) {
                    it.resume(null)
                }
            })
        }
    }

    // 带进度监听的RequestBody
    class ProgressRequestBody(
        private val delegate: RequestBody,
        private val progressCallback: (bytesWritten: Long, contentLength: Long) -> Unit
    ) : RequestBody() {
        override fun contentType(): MediaType? = delegate.contentType()
        override fun contentLength(): Long = delegate.contentLength()
        override fun writeTo(sink: BufferedSink) {
            val countingSink = object : ForwardingSink(sink) {
                var bytesWritten = 0L
                val total = contentLength()
                override fun write(source: okio.Buffer, byteCount: Long) {
                    super.write(source, byteCount)
                    bytesWritten += byteCount
                    progressCallback(bytesWritten, total)
                }
            }
            val bufferedSink = countingSink.buffer()
            delegate.writeTo(bufferedSink)
            bufferedSink.flush()
        }
    }

    /////////////////////////////////////// 以下方法不需要调用 /////////////////////////////////
    override fun onDeleteSelectedPhoto() {
    }

    override fun needRecommendHead(): Boolean {
        return false
    }

    override fun onRecommendHeadSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo) {
    }
}