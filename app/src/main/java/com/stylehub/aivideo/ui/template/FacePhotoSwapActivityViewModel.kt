package com.stylehub.aivideo.ui.template

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.GetImageProgressStateEnum
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ProgressCommonReqModel
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.CustomClothesSwapReqDataModel
import com.stylehub.aivideo.network.model.`in`.FaceSwapTaskReqDataModel
import com.stylehub.aivideo.network.model.`in`.GetImageFacesReqDataModel
import com.stylehub.aivideo.network.model.`in`.VideoFaceSwapTaskReqDataModel
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageFacesRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.utils.ImageUtil
import com.stylehub.aivideo.utils.LoginManager

/**
 *
 * Create by league at 2025/7/2
 *
 * Write some description here
 */
class FacePhotoSwapActivityData {

    /**
     * 步骤
     */
    var step by mutableStateOf(Step.ChoseFace)

    /**
     * 换脸结果
     */
    var swapResult by mutableStateOf(SwapResult.Swapping)

    /**
     * 模板头像
     */
    var templateHeadBitmap by mutableStateOf<Bitmap?>(null)

    /**
     * 所需点数
     */
    var credits by mutableStateOf<Int?>(null)

    /**
     * 带框的合成大图
     */
    var composeBitmap: Bitmap? by mutableStateOf(null)

    /**
     * 选中图片的头像
     */
    var selectHeadBitmap: Bitmap? by mutableStateOf(null)

    /**
     * 推荐头像列表
     */
    var recommendData by mutableStateOf<RecommendHeadListRespDataModel?>(null)

    /**
     * 是否显示进度对话框
     */
    var showProgressDialog by mutableStateOf(false)

    /**
     * 进度值 (0-100)
     */
    var progress by mutableStateOf(0)

    /**
     * 进度文本
     */
    var progressText by mutableStateOf("The uploading process may take a few minutes. Please refrain from closing or exiting the page until itis complete.")

    /**
     * 获取到的脸部信息
     */
    var faceInfo by mutableStateOf<GetImageFacesRespDataModel?>(null)

    enum class Step {

        ChoseFace,
        Uploading,
        BeforeSwap,
        Swapping,
        Swapped
    }

    enum class SwapResult {
        Swapping,
        Fail,
        Success,
    }
}

class FacePhotoSwapActivityViewModel(initialValue: FacePhotoSwapActivityData = FacePhotoSwapActivityData()) :
    BaseViewModel<FacePhotoSwapActivityData>(initialValue) {

    /**
     * ui可变数据
     */
    private var mutableData = _uiStateData.value

    /**
     * 图片模板
     */
    private var imgTemplateModel: GetImageTemplateRespDataModel? = null

    /**
     * 视频模板
     */
    private var videoTemplateModel: FaceSwapVideoTemplateRespDataModel? = null

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
     * 换脸生图任务入参模型
     */
    private var faceSwapTaskReqDataModel: FaceSwapTaskReqDataModel? = null

    /**
     * 视频换脸任务入参模型
     */
    private var videoFaceSwapTaskReqDataModel: VideoFaceSwapTaskReqDataModel? = null

    /**
     * 换衣任务入参模型
     */
    private var clothesSwapReqDataModel: CustomClothesSwapReqDataModel? = null

    /**
     * 任务id
     */
    private var taskId: Long? = null

    /**
     * 设置模板数据
     */
    fun setIntentData(template: Template?) {

        when (template) {
            is GetImageTemplateRespDataModel -> {

                template.apply {

                    imgTemplateModel = this
                    mutableData.credits = getTmpCredits()
                    viewModelScope.launch {
                        val bitmap =
                            ImageUtil.getBitmap(uri = getTmpUrl()?.toUri()) ?: return@launch

                        templateComposedBitmap = ImageUtil.composeHeadBoxIntoBitmap(bitmap, headPos)
                        mutableData.composeBitmap = templateComposedBitmap
                        mutableData.templateHeadBitmap =
                            ImageUtil.getHeaderBitmap(bitmap, headPos)
                    }
                }
            }

            is FaceSwapVideoTemplateRespDataModel -> {

                template.apply {

                    videoTemplateModel = this
                    mutableData.credits = getTmpCredits()
                    viewModelScope.launch {
                        val bitmap = ImageUtil.getBitmap(uri = animate?.toUri()) ?: return@launch
                        val headBitmap =
                            ImageUtil.getBitmap(uri = templateFaceUrl?.toUri()) ?: return@launch

                        templateComposedBitmap = bitmap
                        mutableData.composeBitmap = templateComposedBitmap
                        mutableData.templateHeadBitmap = headBitmap
                    }
                }
            }

            is ClothesTemplateRespDataModel -> {


                template.apply {

                    clothesTemplateModel = this
                    mutableData.credits = getTmpCredits()
                    viewModelScope.launch {
                        val bitmap =
                            ImageUtil.getBitmap(uri = getTmpUrl()?.toUri()) ?: return@launch

                        templateComposedBitmap = bitmap
                        mutableData.composeBitmap = templateComposedBitmap
                        mutableData.templateHeadBitmap = bitmap
                    }
                }

            }
        }
    }

    fun onRecommendSelected(headInfo: RecommendHeadListRespDataModel.HeadInfo) {
        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri = headInfo.url?.toUri()) ?: return@launch
            mutableData.composeBitmap =
                ImageUtil.composeHeadBoxIntoBitmap(bitmap, headInfo.headPos)
            mutableData.selectHeadBitmap =
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

            videoTemplateModel?.let {
                //到此处满足视频换脸的所以条件，创建视频换脸任务模型
                videoFaceSwapTaskReqDataModel = VideoFaceSwapTaskReqDataModel().apply {
                    headType = 1
                    headFaceIndex = headInfo.faceIndex
                    headImg = headInfo.img
                    headPos = headInfo.headPos
                    type = 0 // 视频模板方式
                    templateName = it.getTmpName()
                }
            }

            clothesTemplateModel?.let {
                //创建换衣任务请求模型

                clothesSwapReqDataModel = CustomClothesSwapReqDataModel().apply {
                    srcImg1 = selectedAlbumBitmapBase64
                    srcImg2 = ImageUtil.bitmapImgBase64(mutableData.templateHeadBitmap)
                }
            }
        }
    }

    fun onDeleteSelectedHead() {
        mutableData.composeBitmap = templateComposedBitmap
        mutableData.selectHeadBitmap = null

        //图片换脸请求模型置空
        faceSwapTaskReqDataModel = null
        //视频换脸请求置空
        videoFaceSwapTaskReqDataModel = null
        //换衣请求模型置空
        clothesSwapReqDataModel = null
    }

    fun onAddFromAlbum(uri: Uri?) {
        viewModelScope.launch {
            val bitmap = ImageUtil.getBitmap(uri) ?: return@launch
            val srcImageBase64 = ImageUtil.bitmapImgBase64(bitmap)
            selectedAlbumBitmap = bitmap
            selectedAlbumBitmapBase64 = srcImageBase64
            //调用获取脸部信息接口
            getHeadInfo(srcImageBase64)
        }
    }

    /**
     * 获取脸部信息
     */
    private fun getHeadInfo(srcImgBase64: String) {
        val userId = LoginManager.getUserId() ?: return

        // 显示进度对话框
        mutableData.showProgressDialog = true
        mutableData.progress = 0
        mutableData.progressText = "正在上传图片..."

        // 模拟上传进度
        viewModelScope.launch {
            for (i in 0..100 step 10) {
                mutableData.progress = i
                kotlinx.coroutines.delay(100)
            }
            mutableData.progressText = "正在分析脸部信息..."
            mutableData.progress = 100
        }

        val api = Network().createApi(ApiService::class.java)
        val reqData = GetImageFacesReqDataModel(srcImgBase64)
        val commonReq = ProgressCommonReqModel(reqData)

        api.getImageFaces(commonReq, userId)
            .enqueue(object : retrofit2.Callback<CommonRespModel<GetImageFacesRespDataModel>> {
                override fun onResponse(
                    call: retrofit2.Call<CommonRespModel<GetImageFacesRespDataModel>>,
                    response: retrofit2.Response<CommonRespModel<GetImageFacesRespDataModel>>
                ) {
                    val resp = response.body()
                    if (response.isSuccessful && resp != null && resp.code == 0) {
                        val data = resp.data?.value
                        if (data != null) {
                            mutableData.faceInfo = data
                            // 处理获取到的脸部信息
                            handleFaceInfo(data)
                        }
                    }
                    // 隐藏进度对话框
                    mutableData.showProgressDialog = false
                }

                override fun onFailure(
                    call: retrofit2.Call<CommonRespModel<GetImageFacesRespDataModel>>,
                    t: Throwable
                ) {
                    // 处理失败情况
                    mutableData.showProgressDialog = false
                }
            })
    }

    /**
     * 处理获取到的脸部信息
     */
    private fun handleFaceInfo(faceInfo: GetImageFacesRespDataModel) {
        // 这里可以根据获取到的脸部信息进行相应处理
        // 例如：显示检测到的人脸数量、年龄、性别等信息
        faceInfo.faceInfos?.let { faces ->
            if (faces.isNotEmpty()) {
                // 取第一个检测到的人脸
                val face = faces[0]
                // 可以根据face.x1, face.y1, face.x2, face.y2来裁剪人脸区域
                val pos = "${face.x1},${face.y1},${face.x2},${face.y2}"
                val composedBitmap = ImageUtil.composeHeadBoxIntoBitmap(selectedAlbumBitmap, pos)
                mutableData.composeBitmap = composedBitmap
                mutableData.selectHeadBitmap =
                    ImageUtil.getHeaderBitmap(selectedAlbumBitmap, pos)

                //模板为图片换脸，创建图片换脸的请求模型
                imgTemplateModel?.let {
                    //到此处已经满足图片换脸的所有条件，创建换脸请求模型
                    faceSwapTaskReqDataModel = FaceSwapTaskReqDataModel().apply {
                        headType = 0
                        srcFaceIndex = it.faceIndex
                        headFaceIndex = face.index
                        img = it.getTmpName()
                        resolution =
                            "${mutableData.composeBitmap?.width}*${mutableData.composeBitmap?.height}"
                        headImg = selectedAlbumBitmapBase64
                        headPos = pos

                    }
                }

                //模板为视频换脸，创建视频换脸任务的请求模型
                videoTemplateModel?.let {

                    videoFaceSwapTaskReqDataModel = VideoFaceSwapTaskReqDataModel().apply {
                        headType = 0
                        headFaceIndex = face.index
                        headImg = selectedAlbumBitmapBase64
                        headPos = pos
                        type = 0 // 视频模板方式
                        templateName = it.getTmpName()
                    }
                }

                //模板为换衣，创建换衣任务的请求模型
                clothesTemplateModel?.let {

                    clothesSwapReqDataModel = CustomClothesSwapReqDataModel().apply {
                        srcImg1 = selectedAlbumBitmapBase64
                        srcImg2 = ImageUtil.bitmapImgBase64(mutableData.templateHeadBitmap)
                    }
                }


            }
        }
    }

    fun swapFace() {
        if (mutableData.selectHeadBitmap == null) {
            return
        }
        val userId = LoginManager.getUserId() ?: return

        faceSwapTaskReqDataModel?.let {

            val reqModel = faceSwapTaskReqDataModel ?: return
            val api = Network().createApi(ApiService::class.java)
            mutableData.step = FacePhotoSwapActivityData.Step.Swapping
            mutableData.progress = 0
            mutableData.progressText = "正在创建换脸任务..."
            mutableData.showProgressDialog = true
            val commonReq = CommonReqModel(reqModel)
            api.faceSwapTask(commonReq, userId)
                .enqueue(object :
                    retrofit2.Callback<CommonRespModel<com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            val data = resp.data?.value
                            if (data != null && data.taskId != null) {
                                taskId = data.taskId
                                mutableData.progress = 10
                                mutableData.progressText = "换脸任务已创建，正在排队..."
                                // 可在此处启动轮询进度
                                updateProgress()
                            } else {
                                mutableData.progressText = "任务创建失败，请重试"
                                mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                                mutableData.showProgressDialog = false
                            }
                        } else {
                            mutableData.progressText = resp?.msg ?: "任务创建失败，请重试"
                            mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                            mutableData.showProgressDialog = false
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel>>,
                        t: Throwable
                    ) {
                        mutableData.progressText = "网络异常，任务创建失败"
                        mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                        mutableData.showProgressDialog = false
                    }
                })
        }

        videoFaceSwapTaskReqDataModel?.let {
            val reqModel = videoFaceSwapTaskReqDataModel ?: return
            val api = Network().createApi(ApiService::class.java)
            mutableData.step = FacePhotoSwapActivityData.Step.Swapping
            mutableData.progress = 0
            mutableData.progressText = "正在创建视频换脸任务..."
            mutableData.showProgressDialog = true
            val commonReq = CommonReqModel(reqModel)
            api.createVideoFaceSwapTask(commonReq, userId)
                .enqueue(object :
                    retrofit2.Callback<CommonRespModel<com.stylehub.aivideo.network.model.out.VideoFaceSwapTaskRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.VideoFaceSwapTaskRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<com.stylehub.aivideo.network.model.out.VideoFaceSwapTaskRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            val data = resp.data?.value
                            if (data != null && data.taskId != null) {
                                taskId = data.taskId
                                mutableData.progress = 10
                                mutableData.progressText = "视频换脸任务已创建，正在排队..."
                                // 可在此处启动轮询进度
                                updateProgress()
                            } else {
                                mutableData.progressText = "视频换脸任务创建失败，请重试"
                                mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                                mutableData.showProgressDialog = false
                            }
                        } else {
                            mutableData.progressText = resp?.msg ?: "视频换脸任务创建失败，请重试"
                            mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                            mutableData.showProgressDialog = false
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.VideoFaceSwapTaskRespDataModel>>,
                        t: Throwable
                    ) {
                        mutableData.progressText = "网络异常，视频换脸任务创建失败"
                        mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                        mutableData.showProgressDialog = false
                    }
                })
        }

        clothesSwapReqDataModel?.let {
            val reqModel = clothesSwapReqDataModel ?: return
            val api = Network().createApi(ApiService::class.java)
            mutableData.step = FacePhotoSwapActivityData.Step.Swapping
            mutableData.progress = 0
            mutableData.progressText = "正在创建换衣任务..."
            mutableData.showProgressDialog = true
            val commonReq = CommonReqModel(reqModel)
            api.customClothesSwap(commonReq, userId)
                .enqueue(object :
                    retrofit2.Callback<CommonRespModel<com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel>> {
                    override fun onResponse(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel>>,
                        response: retrofit2.Response<CommonRespModel<com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel>>
                    ) {
                        val resp = response.body()
                        if (response.isSuccessful && resp != null && resp.code == 0) {
                            val data = resp.data?.value
                            if (data != null && data.taskId != null) {
                                taskId = data.taskId
                                mutableData.progress = 10
                                mutableData.progressText = "换衣任务已创建，正在排队..."
                                // 可在此处启动轮询进度
                                updateProgress()
                            } else {
                                mutableData.progressText = "换衣任务创建失败，请重试"
                                mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                                mutableData.showProgressDialog = false
                            }
                        } else {
                            mutableData.progressText = resp?.msg ?: "换衣任务创建失败，请重试"
                            mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                            mutableData.showProgressDialog = false
                        }
                    }

                    override fun onFailure(
                        call: retrofit2.Call<CommonRespModel<com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel>>,
                        t: Throwable
                    ) {
                        mutableData.progressText = "网络异常，换衣任务创建失败"
                        mutableData.step = FacePhotoSwapActivityData.Step.BeforeSwap
                        mutableData.showProgressDialog = false
                    }
                })
        }
    }

    fun updateProgress() {
        val tskId = taskId ?: return
        val userId = LoginManager.getUserId() ?: return

        viewModelScope.launch {

            val api = Network().createApi(ApiService::class.java)
            var ttl = 0
            var isFinished = false
            while (true) {

                if (isFinished || ttl > 10) {
                    mutableData.step = FacePhotoSwapActivityData.Step.Swapped
                    mutableData.swapResult =
                        if (isFinished) FacePhotoSwapActivityData.SwapResult.Success
                        else FacePhotoSwapActivityData.SwapResult.Fail

                    break
                }

                //调用获取进度的接口
                val resp = api.getImageProgress(userId, tskId).execute()
                if (resp.isSuccessful) {
                    resp.body()?.data?.value?.let {
                        if (it.taskId == tskId) {
                            if (it.state == GetImageProgressStateEnum.Making.code) {
                                mutableData.progress = it.progress ?: mutableData.progress
                                ttl = 0
                            } else if (it.state == GetImageProgressStateEnum.Finished.code) {
                                mutableData.progress = 100
                                isFinished = true

                                it.imageInfos?.let {
                                    if (it.isNotEmpty()) {
                                        mutableData.composeBitmap =
                                            ImageUtil.getBitmap(it.get(0).imgUrl?.toUri())
                                    }

                                }

                            } else {
                                ttl++
                            }
                        }
                    }
                } else {
                    ttl++
                }
                delay(500)
            }
        }
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchRecommendHeadList()
    }

    /**
     * 获取推荐头像列表
     */
    private fun fetchRecommendHeadList() {
        val userId = LoginManager.getUserId() ?: return
        val api = Network()
            .createApi(ApiService::class.java)
        api.getRecommendHeadList(userId).enqueue(object :
            retrofit2.Callback<com.stylehub.aivideo.network.model.out.CommonRespModel<RecommendHeadListRespDataModel>> {
            override fun onResponse(
                call: retrofit2.Call<com.stylehub.aivideo.network.model.out.CommonRespModel<RecommendHeadListRespDataModel>>,
                response: retrofit2.Response<com.stylehub.aivideo.network.model.out.CommonRespModel<RecommendHeadListRespDataModel>>
            ) {
                val resp = response.body()
                val data = resp?.data?.value
                if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {
                    mutableData.recommendData = data
                }
            }

            override fun onFailure(
                call: retrofit2.Call<com.stylehub.aivideo.network.model.out.CommonRespModel<RecommendHeadListRespDataModel>>,
                t: Throwable
            ) {
                // 可选：处理异常
            }
        })
    }


}