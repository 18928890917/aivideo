package com.stylehub.aivideo.ui.template

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.stylehub.aivideo.R
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ProgressCommonReqModel
import com.stylehub.aivideo.network.model.`in`.ClayStylizationReqDataModel
import com.stylehub.aivideo.network.model.out.ClayStylizationRespDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.utils.ImageUtil
import com.stylehub.aivideo.utils.LoginManager
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

class ClayStylePhotoSwapActivityUiData : BaseSwapActivityUiData(){
}

class ClayStylePhotoSwapActivityViewModel(
    initialValue: ClayStylePhotoSwapActivityUiData = ClayStylePhotoSwapActivityUiData()
) : BaseSwapActivityViewModel<ClayStylePhotoSwapActivityUiData>(initialValue) {

    private var templateBitmap: Bitmap? = null
    //??从上一页带入
    private var templateUrl: String? = null

    /**
     * 黏土风格任务请求模型
     */
    private var claysStyleTaskReqModel: ClayStylizationReqDataModel? = null

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        templateBitmap = ImageUtil.getBitmap(mActivity!!, R.mipmap.img_clay_style_template)
        mutableData.largeImageBitmap = templateBitmap
//        mutableData.resolutionMap = hashMapOf(pairs = arrayOf(Pair(720, 40), Pair(480, 20)))
        mutableData.title = "Clay Style"

        showBottomBarWithUploadPhotoButton()
    }

    override fun setIntentData(data: Template?) {
        super.setIntentData(data)
    }

    override fun needRecommendHead(): Boolean {
        return false
    }

    override fun onSwapClick() {
        if (claysStyleTaskReqModel == null) {
            return
        }

        showBottomBarWithProgress(0)
        viewModelScope.launch {

            val taskResult : ClayStylizationRespDataModel? = clay()
            claysStyleTaskReqModel = null

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
        download(successDownloadUrl)
    }

    override fun onSelectPhoto(uri: Uri?) {

        viewModelScope.launch {

            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
            mutableData.largeImageBitmap = bitmap
            showBottomBarWithStartMakingButton()

            claysStyleTaskReqModel = ClayStylizationReqDataModel().apply {
                srcImgBase64 = srcImageBase64
            }
        }
    }

    override fun onBackPressed(): Boolean {

        if (isInProgress) {
            showWaitingTooLongDialog()
            return true
        }

        if (claysStyleTaskReqModel != null) {
            claysStyleTaskReqModel = null
            mutableData.largeImageBitmap = templateBitmap
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

    private suspend fun clay(): ClayStylizationRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null
        val reqModel = claysStyleTaskReqModel ?: return null
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

            api.clayStylization(progressRequestBody, userId).enqueue(object : Callback<CommonRespModel<ClayStylizationRespDataModel>> {
                override fun onResponse(
                    call: Call<CommonRespModel<ClayStylizationRespDataModel>>,
                    response: Response<CommonRespModel<ClayStylizationRespDataModel>>
                ) {
                    val resp = response.body()
                    if (response.isSuccessful && resp != null && resp.code == 0) {
                        it.resume(resp.data?.value)
                        return
                    }
                    it.resume(null)
                }

                override fun onFailure(
                    call: Call<CommonRespModel<ClayStylizationRespDataModel>>,
                    t: Throwable
                ) {
                    it.resume(null)
                }

            })
        }

    }

}