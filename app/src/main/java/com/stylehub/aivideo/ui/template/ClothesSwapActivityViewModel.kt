package com.stylehub.aivideo.ui.template

import android.graphics.Bitmap
import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ProgressCommonReqModel
import com.stylehub.aivideo.network.model.`in`.ClothesSwapExReqDataModel
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.CustomClothesSwapReqDataModel
import com.stylehub.aivideo.network.model.out.ClothesSwapExRespDataModel
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.utils.ImageUtil
import com.stylehub.aivideo.utils.LoginManager
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Create by league at 2025/7/2
 *
 * Write some description here
 */
class ClothesSwapActivityUiData : BaseSwapActivityUiData() {
}

class ClothesSwapActivityViewModel(initialValue: ClothesSwapActivityUiData = ClothesSwapActivityUiData()) :
    BaseSwapActivityViewModel<ClothesSwapActivityUiData>(initialValue) {

    private val usingEx = true

    /**
     * 换衣模板
     */
    private var clothesTemplateModel: ClothesTemplateRespDataModel? = null

    /**
     * 模板合成图，用于未选择的时候显示在大图区域
     */
    private var templateComposedBitmap: Bitmap? = null

    /**
     * 已选择的照片，用于在获取完脸部位置的时候直接合成脸部信息
     */
    private var selectedAlbumBitmap: Bitmap? = null
    private var selectedAlbumBitmapBase64: String? = null


    /**
     * 换衣任务入参模型
     */
    private var clothesSwapReqDataModel: CustomClothesSwapReqDataModel? = null
    private var clothesSwapExReqDataModel: ClothesSwapExReqDataModel? = null


    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)

        //下方组件为选择模板类型
        showBottomBarWithTemplate()
    }

    /**
     * 设置模板数据
     */
    override fun setIntentData(data: Template?) {
        super.setIntentData(data)

        if (data is ClothesTemplateRespDataModel) {
            data.run {
                clothesTemplateModel = this
                mutableData.credits = getTmpCredits()
                viewModelScope.launch {
                    val bitmap = ImageUtil.getBitmap(uri = getTmpUrl()?.toUri()) ?: return@launch
                    templateComposedBitmap = bitmap
                    if (mutableData.largeImageBitmap == null) {
                        mutableData.largeImageBitmap = templateComposedBitmap
                    }
                    mutableData.bottomBarLeftHeadBitmap = bitmap
                }
            }
        }
    }


    /**
     * 是否获取推荐头像
     */
    override fun needRecommendHead(): Boolean {
        return false
    }

    /**
     * 选择了推荐头像
     */
    override fun onRecommendHeadSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo) {
    }

    /**
     * 选择了照片
     */
    override fun onSelectPhoto(uri: Uri?) {

        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
            selectedAlbumBitmap = bitmap
            selectedAlbumBitmapBase64 = srcImageBase64
            mutableData.largeImageBitmap = selectedAlbumBitmap
            mutableData.bottomBarRightHeadBitmap = selectedAlbumBitmap

            if (usingEx) {
                clothesSwapExReqDataModel = ClothesSwapExReqDataModel().apply {
                    srcImgBase64 = selectedAlbumBitmapBase64
                }
            } else {
                clothesSwapReqDataModel = CustomClothesSwapReqDataModel().apply {
                    srcImg1 = selectedAlbumBitmapBase64
                    srcImg2 = ImageUtil.bitmapImgBase64(templateComposedBitmap)
                }
            }
        }
    }

    /**
     * 点击了删除所选图片按钮
     */
    override fun onDeleteSelectedPhoto() {

        mutableData.largeImageBitmap = templateComposedBitmap
        mutableData.bottomBarRightHeadBitmap = null
        //换衣请求模型置空
        clothesSwapReqDataModel = null
        clothesSwapExReqDataModel = null
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
        } else {
            updateMiddleProgressContent(progress = progress)
        }
    }

    override fun onSwapClick() {

        //没有选择图片或模板
        if (usingEx) {
            if (clothesSwapExReqDataModel == null) {
                showMessageHintDialog("Please click \"+\" to upload a photo")
                return
            }
        } else {
            if (clothesSwapReqDataModel == null) {
                showMessageHintDialog("Please click \"+\" to upload a photo")
                return
            }
        }

        showMiddleProgressContent(
            mutableData.bottomBarLeftHeadBitmap,
            mutableData.bottomBarRightHeadBitmap
        )
        showBottomBarWithDownloadButtonDisable()

        updateMiddleProgressContent(
            leftBitmap = mutableData.bottomBarLeftHeadBitmap,
            rightBitmap = mutableData.bottomBarRightHeadBitmap
        )
        viewModelScope.launch {

            if (usingEx) {
                val taskResult = swapClothesEx()
                if (taskResult == null) {
                    showMiddleFail()
                    return@launch
                }
                val taskId = taskResult.taskId
                getTaskProgressLoop(taskId)
            } else {
                val taskResult = swapClothes()
                if (taskResult == null) {
                    showMiddleFail()
                    return@launch
                }
                val taskId = taskResult.taskId
                getTaskProgressLoop(taskId)
            }

        }
    }

    override fun onDownloadClick() {
        if (successDownloadUrl != null) {
            download(successDownloadUrl)
        }
    }


    /**
     * 执行换衣任务
     */
    private suspend fun swapClothesEx(): ClothesSwapExRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null
        val reqModel = clothesSwapExReqDataModel ?: return null
        if (clothesTemplateModel?.templateName == null) {
            return null
        }
        val api = Network().createApi(ApiService::class.java)

        return suspendCoroutine {

            val commonReq = ProgressCommonReqModel(reqModel) {
                    bytesWritten: Long, contentLength: Long, done: Boolean ->

                val currentProgress = bytesWritten * 100 / contentLength

                //添加延迟使动画更流畅
                startDelayProgress(currentProgress.toInt()) {
                    progress ->
                    if (progress < 100) {
                        showUploadDialog(progress, "Uploading...")
                    } else {
                        showUploadDialog(100, "Creating task...")
                    }
                }

                if (done) {
                    startDelayProgress(100)
                }
            }
            api.clothesSwapEx(commonReq, userId, clothesTemplateModel!!.templateName)
                .enqueue(object : retrofit2.Callback<CommonRespModel<ClothesSwapExRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<ClothesSwapExRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<ClothesSwapExRespDataModel>>
                    ) {
                        val resp = response.body()
                        dismissUploadDialog()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            it.resume(resp.data?.value)
                            return
                        }
                        it.resume(null)
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<ClothesSwapExRespDataModel>>,
                        t: Throwable
                    ) {
                        dismissUploadDialog()
                        it.resume(null)
                    }
                })
        }

    }


    /**
     * 执行换衣任务
     */
    private suspend fun swapClothes(): CustomClothesSwapRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null
        val reqModel = clothesSwapReqDataModel ?: return null
        val api = Network().createApi(ApiService::class.java)

        return suspendCoroutine {

            val commonReq = CommonReqModel(reqModel)
            api.customClothesSwap(commonReq, userId)
                .enqueue(object :
                    retrofit2.Callback<CommonRespModel<CustomClothesSwapRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<CustomClothesSwapRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<CustomClothesSwapRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            it.resume(resp.data?.value)
                            return
                        }
                        it.resume(null)
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<CustomClothesSwapRespDataModel>>,
                        t: Throwable
                    ) {
                        it.resume(null)
                    }
                })
        }

    }


}