package com.stylehub.aivideo.ui.template

import android.content.ContentResolver
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.stylehub.aivideo.R
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ProgressCommonReqModel
import com.stylehub.aivideo.network.model.`in`.CreateImageTaskReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.CreateImageTaskRespDataModel
import com.stylehub.aivideo.network.model.out.CreateImageTaskTemplateModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.utils.ImageUtil
import com.stylehub.aivideo.utils.LoginManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Create by league at 2025/7/2
 *
 */

class CommonPhotoSwapActivityUiData : BaseSwapActivityUiData()

class CommonSwapActivityViewModel(
    initialValue: CommonPhotoSwapActivityUiData = CommonPhotoSwapActivityUiData()
) : BaseSwapActivityViewModel<CommonPhotoSwapActivityUiData>(initialValue) {

    private var imageTaskTemplate: CreateImageTaskTemplateModel? = null
    private var src1ImgUri: Uri? = null
    /**
     * 黏土风格任务请求模型
     */
    private var createImgTaskReqModel = CreateImageTaskReqDataModel()

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        showBottomBarWithUploadPhotoButton()
    }

    override fun setIntentData(data: Template?) {
        super.setIntentData(data)
        imageTaskTemplate = mTemplate as CreateImageTaskTemplateModel?

        imageTaskTemplate?.run {
            mutableData.title = title?: taskType
            mutableData.subTitle = subTitle
            mutableData.showCredits = false

            if (isVideoPreview) {
                mutableData.largeVideoUrl = getPreviewUrl()
            } else {
                if (getPreviewUrl().isEmpty()) {
                    mutableData.largeImageUrl = Uri.Builder()
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .authority(mActivity!!.packageName)
                        .appendPath(R.mipmap.img_clay_style_template.toString())
                        .build()
                        .toString()
                } else {
                    mutableData.largeImageUrl = getPreviewUrl()
                }
            }

            if (size1 != null) {
                if (size2 != null) {
                    mutableData.resolutionMap = mutableMapOf<String, Int>().apply {
                        put("$size1${if (isFpsSize) "FPS" else ""}", credits1?:1)
                        put("$size2${if (isFpsSize) "FPS" else ""}", credits2?:1)
                    }
                } else {
                    createImgTaskReqModel.size = size1
                }
            }

            //设置请求模型
            createImgTaskReqModel.taskType = taskType
            createImgTaskReqModel.needOpt = needOpt
            createImgTaskReqModel.templateName = templateName
            createImgTaskReqModel.ext = ext
        }
    }

    override fun needRecommendHead(): Boolean {
        return false
    }

    override fun onSwapClick() {

        if (createImgTaskReqModel.srcImg1 == null) {
            return
        }

        showBottomBarWithProgress(0)
        viewModelScope.launch {

            val taskResult : CreateImageTaskRespDataModel? = createImageTask()

            createImgTaskReqModel.srcImg1 = null
            createImgTaskReqModel.srcImg2 = null

            if (taskResult == null) {
                showMiddleFail()
                showBottomBarWithDownloadButtonDisable()
                return@launch
            }
            val taskId = taskResult.taskId
            getTaskProgressLoop(taskId)
        }
    }

    override fun onSwapClick(resolution: String?, credits: Int?) {

        createImgTaskReqModel.size = resolution?.replace("FPS", "", true)
        super.onSwapClick(resolution, credits)
    }

    override fun onProgress(isFinished: Boolean, isFail: Boolean, progress: Int) {
        if (isFinished) {
            viewModelScope.launch {
                showMiddleLoading()
                showBottomBarWithDownloadButton()
                val successBitmap = ImageUtil.getBitmap(successDownloadUrl!!.toUri())
                mutableData.largeImageBitmap = successBitmap
                if (successBitmap == null) {
                    mutableData.successVideoUrl = successDownloadUrl
                }
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
        download(successDownloadUrl)
    }

    override fun onSelectPhoto(uri: Uri?) {

        viewModelScope.launch {

            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
            mutableData.largeImageBitmap = bitmap

            if (createImgTaskReqModel.srcImg1 == null) {
                src1ImgUri = uri
                createImgTaskReqModel.srcImg1 = srcImageBase64

                if (imageTaskTemplate?.srcImg2Support == true) {
                    mutableData.subTitle = "photo1(select another photo or skip)"
                    showBottomBarWithUploadPhotoButton(true)
                } else {
                    mutableData.subTitle = "photo"
                    if (imageTaskTemplate?.size2 != null) {
                        showBottomBarWithResolution()
                    } else {
                        showBottomBarWithStartMakingButton()
                    }
                }
            } else {
                mutableData.subTitle = "photo2"
                createImgTaskReqModel.srcImg2 = srcImageBase64
                if (imageTaskTemplate?.size2 != null) {
                    showBottomBarWithResolution()
                } else {
                    showBottomBarWithStartMakingButton()
                }
            }

        }
    }

    override fun onSkipClick() {

        mutableData.subTitle = "photo"
        if (imageTaskTemplate?.size2 != null) {
            showBottomBarWithResolution()
        } else {
            showBottomBarWithStartMakingButton()
        }
    }

    override fun onBackPressed(): Boolean {

        if (isInProgress) {
            showWaitingTooLongDialog()
            return true
        }

        if (createImgTaskReqModel.srcImg2 != null) {
            createImgTaskReqModel.srcImg2 = null
            createImgTaskReqModel.srcImg1 = null
            onSelectPhoto(src1ImgUri)
            showMiddlePic()
            return true
        } else if (createImgTaskReqModel.srcImg1 != null) {
            createImgTaskReqModel.srcImg1 = null
            mutableData.largeImageBitmap = null
            mutableData.subTitle = "Template"
            showBottomBarWithUploadPhotoButton()
            showMiddlePic()
            return true
        }
        return false
    }

    override fun onRecommendHeadSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo) {
        //do nothing
    }
    override fun onDeleteSelectedPhoto() {
        //do nothing
    }

    private suspend fun createImageTask(): CreateImageTaskRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null
        val reqModel = createImgTaskReqModel
        return suspendCoroutine {

            val progressRequestBody = ProgressCommonReqModel(reqModel) {
                    bytesWritten: Long, contentLength: Long, done: Boolean->

                val currentProgress = bytesWritten * 100 / contentLength
                showUploadDialog(currentProgress.toInt(), "uploading...")

                if (done) {
                    dismissUploadDialog()
                }
            }

            val api = Network().createApi(ApiService::class.java)

            api.createImageTask(progressRequestBody, userId).enqueue(object : Callback<CommonRespModel<CreateImageTaskRespDataModel>> {
                override fun onResponse(
                    call: Call<CommonRespModel<CreateImageTaskRespDataModel>>,
                    response: Response<CommonRespModel<CreateImageTaskRespDataModel>>
                ) {
                    val resp = response.body()
                    if (response.isSuccessful && resp != null && resp.code == 0) {
                        it.resume(resp.data?.value)
                        return
                    } else if (resp != null && resp.code != 0) {
                        showMessageHintDialog("${resp.msg?:""}[Error Code:${resp.code}]", "error")
                    }
                    it.resume(null)
                }

                override fun onFailure(
                    call: Call<CommonRespModel<CreateImageTaskRespDataModel>>,
                    t: Throwable
                ) {
                    it.resume(null)
                }

            })
        }

    }

}