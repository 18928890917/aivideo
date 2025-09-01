package com.stylehub.aivideo.ui.template

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.FaceSwapTaskReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageFacesRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
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
class ImageFaceSwapActivityData: BaseSwapActivityUiData()

class ImageFaceSwapActivityViewModel(initialValue: ImageFaceSwapActivityData = ImageFaceSwapActivityData()) :
    BaseSwapActivityViewModel<ImageFaceSwapActivityData>(initialValue) {

    /**
     * 图片模板
     */
    private var imgTemplateModel: GetImageTemplateRespDataModel? = null

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
     * 换脸生图任务入参模型
     */
    private var faceSwapTaskReqDataModel: FaceSwapTaskReqDataModel? = null



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

        if (data is GetImageTemplateRespDataModel) {
            data.apply {

                imgTemplateModel = this
                mutableData.credits = getTmpCredits()
                viewModelScope.launch {
                    val bitmap = ImageUtil.getBitmap(uri = getTmpUrl()?.toUri()) ?: return@launch

                    if (headPos == null) {

                        mutableData.largeImageBitmap = bitmap

                        val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
                        val faceInfo = getFaceInfo(srcImageBase64)
                        if (faceInfo != null && !faceInfo.faceInfos.isNullOrEmpty()) {
                            val face = faceInfo.faceInfos[0]
                            // 可以根据face.x1, face.y1, face.x2, face.y2来裁剪人脸区域
                            headPos = "${face.x1},${face.y1},${face.x2},${face.y2}"
                        }
                    }

                    templateComposedBitmap = ImageUtil.composeHeadBoxIntoBitmap(bitmap, headPos)
                    if (mutableData.largeImageBitmap == null) {
                        mutableData.largeImageBitmap = templateComposedBitmap?:bitmap
                    }
                    mutableData.bottomBarLeftHeadBitmap =
                        ImageUtil.getHeaderBitmap(bitmap, headPos)
                }
            }
        }
    }

    /**
     * 是否获取推荐头像
     */
    override fun needRecommendHead(): Boolean {
        return true
    }

    /**
     * 选择了推荐头像
     */
    override fun onRecommendHeadSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo) {

        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri = headInfo.url?.toUri()) ?: return@launch
            mutableData.largeImageBitmap =
                ImageUtil.composeHeadBoxIntoBitmap(bitmap, headInfo.headPos)
            mutableData.bottomBarRightHeadBitmap =
                ImageUtil.getHeaderBitmap(bitmap, headInfo.headPos)

            imgTemplateModel?.let {
                //到此处已经满足图片换脸的所有条件，创建换脸请求模型
                faceSwapTaskReqDataModel = FaceSwapTaskReqDataModel().apply {
                    headType = 1
                    srcFaceIndex = it.faceIndex
                    headFaceIndex = headInfo.faceIndex
                    img = it.getTmpName()
                    resolution = it.resulotion
                    headImg = headInfo.img
                    headPos = headInfo.headPos

                }
            }
        }
    }

    /**
     * 选择了图片
     */
    override fun onSelectPhoto(uri: Uri?) {

        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
            selectedAlbumBitmap = bitmap
            selectedAlbumBitmapBase64 = srcImageBase64

            // 显示进度对话框
            val dialogHint = "The uploading process may take a few minutes. Please refrain from closing or exiting the page until itis complete."
            showUploadDialog(0, dialogHint)

            //调用获取脸部信息接口
            val faceInfo = getFaceInfo(srcImageBase64) {
                    bytesWritten: Long, contentLength: Long, done: Boolean ->
                //上传进度
                val currentProgress = bytesWritten * 100 / contentLength

                startDelayProgress(currentProgress.toInt()) { progress: Int ->
                    if (progress == 100) {
                        showUploadDialog(delayProgress, "analysing face...")
                    } else {
                        showUploadDialog(progress, dialogHint)
                    }
                }
                if (done) {
                    startDelayProgress(100)
                }
            }
            stopDelayProgress()
            if (faceInfo == null) {
                viewModelScope.launch {
                    showUploadDialog(100, "No face info")
                    delay(1000)
                    dismissUploadDialog()
                }
                return@launch
            }
            // 处理获取到的脸部信息
            val handleResult = handleFaceInfo(faceInfo)
            if (!handleResult) {
                viewModelScope.launch {
                    showUploadDialog(1000, "No face info")
                    delay(1000)
                    dismissUploadDialog()
                }
            } else {
                dismissUploadDialog()
            }
        }
    }

    /**
     * 处理获取到的脸部信息
     */
    private fun handleFaceInfo(faceInfo: GetImageFacesRespDataModel): Boolean {
        // 这里可以根据获取到的脸部信息进行相应处理
        // 例如：显示检测到的人脸数量、年龄、性别等信息

        if (!faceInfo.faceInfos.isNullOrEmpty()) {
            // 取第一个检测到的人脸
            val face = faceInfo.faceInfos[0]

            // 可以根据face.x1, face.y1, face.x2, face.y2来裁剪人脸区域
            val pos = "${face.x1},${face.y1},${face.x2},${face.y2}"
            val composedBitmap = ImageUtil.composeHeadBoxIntoBitmap(selectedAlbumBitmap, pos)
            mutableData.largeImageBitmap = composedBitmap
            mutableData.bottomBarRightHeadBitmap =
                ImageUtil.getHeaderBitmap(selectedAlbumBitmap, pos)

            //模板为图片换脸，创建图片换脸的请求模型
            imgTemplateModel?.let {
                //到此处已经满足图片换脸的所有条件，创建换脸请求模型
                faceSwapTaskReqDataModel = FaceSwapTaskReqDataModel().apply {
                    headType = 0
                    srcFaceIndex = it.faceIndex?:0
                    headFaceIndex = face.index
                    img = it.getTmpName()
                    resolution = "${mutableData.largeImageBitmap?.width}*${mutableData.largeImageBitmap?.height}"
                    headImg = ImageUtil.bitmapImgBase64(selectedAlbumBitmap)
                    headPos = pos

                }
            }
            return true
        }
        return false

    }

    /**
     * 点击了删除所选图片按钮
     */
    override fun onDeleteSelectedPhoto() {

        mutableData.largeImageBitmap = templateComposedBitmap
        mutableData.bottomBarRightHeadBitmap = null
        //图片换脸请求模型置空
        faceSwapTaskReqDataModel = null
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

    /**
     * 点击了换脸按钮
     */
    override fun onSwapClick() {

        //没有选择图片或模板
        if (faceSwapTaskReqDataModel == null) {
            showMessageHintDialog("Please click \"+\" to upload a face photo")
            return
        }

        showMiddleProgressContent(mutableData.bottomBarLeftHeadBitmap, mutableData.bottomBarRightHeadBitmap)
        showBottomBarWithDownloadButtonDisable()

        updateMiddleProgressContent(leftBitmap = mutableData.bottomBarLeftHeadBitmap, rightBitmap = mutableData.bottomBarRightHeadBitmap)
        viewModelScope.launch {
            val taskResult = swapFace()
            if (taskResult == null) {
                showMiddleFail()
                return@launch
            }
            val taskId = taskResult.taskId
            getTaskProgressLoop(taskId)
        }
    }

    /**
     * 调用换脸接口
     */
    private suspend fun swapFace() : FaceSwapTaskRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null
        if (faceSwapTaskReqDataModel == null) return null

        val api = Network().createApi(ApiService::class.java)
        val commonReq = CommonReqModel(faceSwapTaskReqDataModel!!)

        return suspendCoroutine {

            api.faceSwapTask(commonReq, userId)
                .enqueue(object : retrofit2.Callback<CommonRespModel<FaceSwapTaskRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<FaceSwapTaskRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<FaceSwapTaskRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            it.resume(resp.data?.value)
                            return
                        }
                        it.resume(null)
                    }
                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<FaceSwapTaskRespDataModel>>,
                        t: Throwable
                    ) {
                        it.resume(null)
                    }
                })
        }
    }

    /**
     * 点击了下载按钮
     */
    override fun onDownloadClick() {

        if (successDownloadUrl != null) {
            download(successDownloadUrl)
        }
    }

}