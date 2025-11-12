package com.stylehub.aivideo.network

import com.stylehub.aivideo.network.consts.AppConst
import com.stylehub.aivideo.network.model.`in`.AppEventReqDataModel
import com.stylehub.aivideo.network.model.`in`.ClayStylizationReqDataModel
import com.stylehub.aivideo.network.model.`in`.ClothesSwapExReqDataModel
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.CreateImageTaskReqDataModel
import com.stylehub.aivideo.network.model.`in`.CreatePaymentReqDataModel
import com.stylehub.aivideo.network.model.`in`.CustomClothesSwapReqDataModel
import com.stylehub.aivideo.network.model.`in`.DeleteAccountReqDataModel
import com.stylehub.aivideo.network.model.`in`.FaceSwapExTaskReqDataModel
import com.stylehub.aivideo.network.model.`in`.FaceSwapTaskReqDataModel
import com.stylehub.aivideo.network.model.`in`.FastLoginReqDataModel
import com.stylehub.aivideo.network.model.`in`.FeedbackReadReqDataModel
import com.stylehub.aivideo.network.model.`in`.FeedbackSubmitReqDataModel
import com.stylehub.aivideo.network.model.`in`.GetImageFacesReqDataModel
import com.stylehub.aivideo.network.model.`in`.GetPaymentDetailListReqDataModel
import com.stylehub.aivideo.network.model.`in`.GetVideoFacesReqDataModel
import com.stylehub.aivideo.network.model.`in`.GoogleLoginReqDataModel
import com.stylehub.aivideo.network.model.`in`.GooglePayCallbackReqDataModel
import com.stylehub.aivideo.network.model.`in`.OfftopVideoReqDataModel
import com.stylehub.aivideo.network.model.`in`.ReportReferrerReqDataModel
import com.stylehub.aivideo.network.model.`in`.Txt2ImgCreateReqDataModel
import com.stylehub.aivideo.network.model.`in`.Txt2ImgPromptsReqDataModel
import com.stylehub.aivideo.network.model.`in`.VideoFaceSwapTaskReqDataModel
import com.stylehub.aivideo.network.model.out.CategoryListRespDataModel
import com.stylehub.aivideo.network.model.out.ClayStylizationRespDataModel
import com.stylehub.aivideo.network.model.out.ClothesSwapExRespDataModel
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.CreateImageTaskRespDataModel
import com.stylehub.aivideo.network.model.out.CreatePaymentRespDataModel
import com.stylehub.aivideo.network.model.out.CreditsPageRespDataModel
import com.stylehub.aivideo.network.model.out.CustomClothesSwapRespDataModel
import com.stylehub.aivideo.network.model.out.FaceSwapExTaskRespDataModel
import com.stylehub.aivideo.network.model.out.FaceSwapTaskRespDataModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.FeedbackListRespDataModel
import com.stylehub.aivideo.network.model.out.GetGooglePayActivitiesRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageFacesRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageProgressRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImg2VideoPoseTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetPaymentDetailListRespDataModel
import com.stylehub.aivideo.network.model.out.GetVideoFacesRespDataModel
import com.stylehub.aivideo.network.model.out.GooglePayCallbackRespDataModel
import com.stylehub.aivideo.network.model.out.ImagePromptRecommendRespDataModel
import com.stylehub.aivideo.network.model.out.Img2VideoPoseTaskRespDataModel
import com.stylehub.aivideo.network.model.out.LoginResp
import com.stylehub.aivideo.network.model.out.MyCreationRespDataModel
import com.stylehub.aivideo.network.model.out.MyTasksRespDataModel
import com.stylehub.aivideo.network.model.out.OfftopVideoRespDataModel
import com.stylehub.aivideo.network.model.out.RecommendHeadListRespDataModel
import com.stylehub.aivideo.network.model.out.Txt2ImgCreateRespDataModel
import com.stylehub.aivideo.network.model.out.Txt2ImgPromptsRespDataModel
import com.stylehub.aivideo.network.model.out.Txt2ImgTagsRespDataModel
import com.stylehub.aivideo.network.model.out.UserCommonInfoRespDataModel
import com.stylehub.aivideo.network.model.out.UserPaymentsRespDataModel
import com.stylehub.aivideo.network.model.out.VideoFaceSwapTaskRespDataModel
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

/**
 *
 * Create by league at 2025/6/23
 *
 * Write some description here
 */


interface ApiService {

    /**
     * 设备快速登录
     */
    @POST("/v1/user/fast_login")
    fun fastLogin(
        @Body body: CommonReqModel<FastLoginReqDataModel>,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<LoginResp>>

    /**
     * 分类列表
     */
    @GET("/v1/image/category-list")
    fun categoryList(
        @Query("pkg") pkg: String = AppConst.packageName,//包名，必填
        @Query("templateTypes") templateTypes: String? = null,//"模板类型，不传默认查全部"
    ): Call<CommonRespModel<List<CategoryListRespDataModel>>>

    /**
     * 黏土风格图片任务创建
     */
    @POST("/v1/image/clay_stylization")
    fun clayStylization(
        @Body body: ProgressCommonReqModel<ClayStylizationReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<ClayStylizationRespDataModel>>

    /**
     * 请求创建换衣生图任务
     */
    @POST("/v1/image/clothes_swap_ex")
    fun clothesSwapEx(
        @Body body: ProgressCommonReqModel<ClothesSwapExReqDataModel>,
        @Query("userId") userId: String,
        @Query("templateName") templateName: String? = null,
        @Query("type") type: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<ClothesSwapExRespDataModel>>

    /**
     * 获取换衣模板
     */
    @GET("/v1/image/clothes_template")
    fun getClothesTemplates(
        @Query("userId") userId: String,
//        @Query("categoryId") categoryId: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<MutableList<ClothesTemplateRespDataModel>>>

    /**
     * 创建支付订单
     * 入参/出参 JSON 见 CreatePaymentReqDataModel / CreatePaymentRespDataModel 注释
     */
    @POST("/v1/payment/createPayment")
    fun createPayment(
        @Body body: CommonReqModel<CreatePaymentReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<CreatePaymentRespDataModel>>

    /**
     * 通用图生任务
     * 入参/出参 JSON 见 CreateImageTaskReqDataModel / CreateImageTaskRespDataModel 注释
     */
    @POST("/v1/image/create-task")
    fun createImageTask(
        @Body body: ProgressCommonReqModel<CreateImageTaskReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<CreateImageTaskRespDataModel>>

    /**
     * 自定义换衣
     * 入参/出参 JSON 见 CustomClothesSwapReqDataModel / CustomClothesSwapRespDataModel 注释
     */
    @POST("/v1/image/custom_clothes_swap")
    fun customClothesSwap(
        @Body body: CommonReqModel<CustomClothesSwapReqDataModel>,
        @Query("userId") userId: String,
        @Query("type") type: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<CustomClothesSwapRespDataModel>>

    /**
     * 高级图片换脸
     * 入参/出参 JSON 见 FaceSwapExTaskReqDataModel / FaceSwapExTaskRespDataModel 注释
     */
    @POST("/v1/image/faceswap_ex_task")
    fun faceSwapExTask(
        @Body body: CommonReqModel<FaceSwapExTaskReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<FaceSwapExTaskRespDataModel>>

    /**
     * 请求创建换脸生图任务
     * 入参/出参 JSON 见 FaceSwapTaskReqDataModel / FaceSwapTaskRespDataModel 注释
     */
    @POST("/v1/image/faceswap_task")
    fun faceSwapTask(
        @Body body: CommonReqModel<FaceSwapTaskReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<FaceSwapTaskRespDataModel>>

    /**
     * 视频换脸模版获取
     */
    @GET("/v1/image/faceswap_video_template")
    fun getFaceSwapVideoTemplates(
        @Query("userId") userId: String,
        @Query("ch") ch: Int = 1,
//        @Query("categoryId") categoryId: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<FaceSwapVideoTemplateRespDataModel>>>

    /**
     * 脸部信息获取
     * 入参/出参 JSON 见 GetImageFacesReqDataModel / GetImageFacesRespDataModel 注释
     */
    @POST("/v1/image/get_image_faces")
    fun getImageFaces(
        @Body body: ProgressCommonReqModel<GetImageFacesReqDataModel>,
        @Query("userId") userId: String,
//        @Query("ch") ch: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<GetImageFacesRespDataModel>>

    /**
     * 视频信息获取（上传视频）
     * 入参/出参 JSON 见 GetVideoFacesReqDataModel / GetVideoFacesRespDataModel 注释
     */
    @POST("/v1/image/get_video_faces")
    fun getVideoFaces(
        @Body body: CommonReqModel<GetVideoFacesReqDataModel>,
        @Query("userId") userId: String,
        @Query("ch") ch: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<GetVideoFacesRespDataModel>>

    /**
     * 获取支付订单
     * 入参/出参 JSON 见 GetPaymentDetailListReqDataModel / GetPaymentDetailListRespDataModel 注释
     */
    @POST("/v1/payment/getPaymentDetailList")
    fun getPaymentDetailList(
        @Body body: CommonReqModel<GetPaymentDetailListReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<GetPaymentDetailListRespDataModel>>>

    /**
     * Google登录
     * 入参/出参 JSON 见 GoogleLoginReqDataModel / GoogleLoginRespDataModel 注释
     */
    @POST("/v1/user/login/google")
    fun googleLogin(
        @Body body: CommonReqModel<GoogleLoginReqDataModel>,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<LoginResp>>

    /**
     * 安卓获取商品列表
     */
    @GET("/v1/payment/getGooglePayActivities")
    fun getGooglePayActivities(
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app
    ): Call<CommonRespModel<GetGooglePayActivitiesRespDataModel>>

    /**
     * 获取推荐图片模板(图片换脸模板)
     */
    @GET("/v1/image/image_template")
    fun getImageTemplates(
        @Query("userId") userId: String,
        @Query("ch") ch: Int = 1,
        @Query("category") category: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<GetImageTemplateRespDataModel>>>

    /**
     * 图片生成视频pose
     */
    @Multipart
    @POST("/v1/image/img2video_pose_task")
    fun img2VideoPoseTask(
        @Part("wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904") srcImgBase64: RequestBody?,
        @Part("wxysvwxrghicZABVmnoihijdijkestuo") srcVideo: RequestBody? = null,
        @Query("userId") userId: String,
        @Query("templateName") templateName: String = "",
        @Query("fps") fps: Int = 25,
        @Query("needopt") needopt: Boolean = false,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<Img2VideoPoseTaskRespDataModel>>

    /**
     * 图片生成视频post模版
     */
    @GET("/v1/image/img2Video_pose_template")
    fun getImg2VideoPoseTemplates(
        @Query("userId") userId: String,
        @Query("ch") ch: Int = 1,
//        @Query("categoryId") categoryId: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<GetImg2VideoPoseTemplateRespDataModel>>>

    /**
     * 启动事件上报
     * query: event=appLauncher&userId=cxx&ch=xx&deviceId=xxx&app=android&pkg=xxxxxx
     * 入参/出参 JSON 见 AppEventReqDataModel 注释
     */
    @POST("/v1/log/appevent")
    fun reportAppEvent(
        @Body body: CommonReqModel<AppEventReqDataModel>,
        @Query("deviceId") deviceId: String,
        @Query("event") event: String = AppConst.appName,
        @Query("domain") ch: String = AppConst.packageName,
    ): Call<CommonRespModel<Unit>>

    /**
     * 获取用户自己的生成作品列表
     * query: app="android",page:页数，size:每页个数，taskType:需要过滤的任务类型，以逗号分割；如果需要全部，不传次参数
     * 出参 JSON 见 MyCreationRespDataModel 注释
     */
    @GET("/v1/image/mycreation")
    fun myCreation(
        @Query("userId") userId: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("taskType") taskType: String? = null,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<MyCreationRespDataModel>>

    /**
     * 我的任务列表（含进行中和已完成）
     * query:
     * app="android",
     * page:页数，
     * size:每页个数，
     * taskType:需要过滤的任务类型，以逗号分割；如果需要全部，不传次参数；2=图片换脸，3=换衣，5=视频换脸，7=黏土风格，8=跳舞视频，11=高级换脸
     * 出参 JSON 见 MyTasksRespDataModel 注释
     */
    @GET("/v1/image/my-tasks")
    fun myTasks(
        @Query("page") page: Int,
        @Query("taskTypes") taskType: String? = "2",
        @Query("size") size: Int = 20,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<MyTasksRespDataModel>>

    /**
     * 图片动作风格化gif
     * 请调用通用图生任务接口！
     *
     * query: ?userId=xxxx&app=xxxx&needopt=false
     * 入参/出参 JSON 见 OfftopVideoReqDataModel / OfftopVideoRespDataModel 注释
     */
    @POST("/v1/image/offtop_video")
    fun offtopVideo(
        @Body body: CommonReqModel<OfftopVideoReqDataModel>,
        @Query("userId") userId: String,
        @Query("needopt") needopt: Boolean = false,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<OfftopVideoRespDataModel>>

    /**
     * 谷歌支付成功结果回调
     * query: ?userId=xxxx&app=android
     * 入参/出参 JSON 见 GooglePayCallbackReqDataModel / GooglePayCallbackRespDataModel 注释
     */
    @POST("/v1/payment/googlepay")
    fun googlePayCallback(
        @Body body: CommonReqModel<GooglePayCallbackReqDataModel>,
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<GooglePayCallbackRespDataModel>>

    /**
     * 获取文生图推荐提示
     * query: app="android", ch=1
     * 出参JSON见 ImagePromptRecommendRespDataModel 注释
     */
    @GET("/v1/image/prompt/recomends")
    fun getImagePromptRecommends(
        @Query("ch") ch: Int = 1,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<ImagePromptRecommendRespDataModel>>>

    /**
     * 获取推荐头像列表
     * query: app="android", userId=xxxx
     * 出参JSON见 RecommendHeadListRespDataModel 注释
     */
    @GET("/v1/image/recommendhead")
    fun getRecommendHeadList(
        @Query("userId") userId: String,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<RecommendHeadListRespDataModel>>

    /**
     * 上报一次归因信息
     * query: pkg=AppConst.packageName, app="android"
     * 入参JSON见 ReportReferrerReqDataModel 注释
     */
    @POST("/v1/user/referrer")
    fun reportReferrer(
        @Body body: CommonReqModel<ReportReferrerReqDataModel>,
        @Query("type") type: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<Unit>>

    /**
     * 请求创建生成文生图任务
     * query: pkg=AppConst.packageName, app="android", userId
     * 入参JSON见 Txt2ImgCreateReqDataModel 注释，出参JSON见 Txt2ImgCreateRespDataModel 注释
     */
    @POST("/v1/image/txt2img_create")
    fun createTxt2ImgTask(
        @Body body: CommonReqModel<Txt2ImgCreateReqDataModel>,
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<Txt2ImgCreateRespDataModel>>

    /**
     * 文生图提示语润色接口
     * query: pkg=AppConst.packageName, app="android", userId
     * 入参JSON见 Txt2ImgPromptsReqDataModel 注释，出参JSON见 Txt2ImgPromptsRespDataModel 注释
     */
    @POST("/v1/image/txt2img_prompts")
    fun polishTxt2ImgPrompt(
        @Body body: CommonReqModel<Txt2ImgPromptsReqDataModel>,
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<Txt2ImgPromptsRespDataModel>>

    /**
     * 文生图tag模板
     * query: app="android", userId=xxxx
     * 出参JSON见 Txt2ImgTagsRespDataModel 注释
     */
    @GET("/v1/image/txt2img_tags")
    fun getTxt2ImgTags(
        @Query("app") app: String = AppConst.app,
        @Query("userId") userId: String
    ): Call<CommonRespModel<List<Txt2ImgTagsRespDataModel>>>

    /**
     * 获取用户通用信息
     * query: pkg=AppConst.packageName, app="android", userId=xxxx
     * 出参JSON见 UserCommonInfoRespDataModel 注释
     */
    @GET("/v1/user/common_info")
    fun getUserCommonInfo(
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<UserCommonInfoRespDataModel>>

    /**
     * 积分记录
     * query:
     * {
     *     "page": "选填，页码，默认1",
     *     "size": "选填，每页条数，默认10",
     *     "type": "选填，积分类型，1=增加，2=扣除",
     *     "businessTypes": "选填，多个用,分割，例如admin,register 业务类型：task=任务，recharge=充值，admin=管理员操作，register=注册"
     * }
     *
     * 出参JSON见 CreditsPageRespDataModel 注释
     */
    @GET("/v1/user/credits-page")
    fun getCreditsPage(
        @Query("userId") userId: String,
        @Query("type") type: Int? = null,
        @Query("page") page: Int? = 1,
        @Query("size") size: Int? = 10,
        @Query("businessTypes") businessTypes: String? = null,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<CreditsPageRespDataModel>>

    /**
     * 获取用户的付费成功记录
     * query: pkg=AppConst.packageName, app="android", userId=xxxx
     * 出参JSON见 UserPaymentsRespDataModel 注释
     */
    @GET("/v1/user/payments")
    fun getUserPayments(
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<List<UserPaymentsRespDataModel>>>


    /**
     * 6.请求图片生成进度
     *
     * query: pkg=AppConst.packageName, app="android", userId=xxxx
     * 出参JSON见 UserPaymentsRespDataModel 注释
     */
    @GET("/v1/image/progress")
    fun getImageProgress(
        @Query("userId") userId: String,
        @Query("taskId") taskId: Long,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<GetImageProgressRespDataModel>>

    /**
     * 获取用户账户信息
     * query: pkg=AppConst.packageName, app="android", userId=xxxx, ch=xxx
     * 出参JSON见 UserAccountRespDataModel 注释
     */
    @GET("/v1/user/account")
    fun getUserAccount(
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<LoginResp>>

    /**
     * 删除账号
     * query: pkg=AppConst.packageName, app="android", userId
     * 入参JSON见 DeleteAccountReqDataModel 注释
     */
    @POST("/v1/user/delete")
    fun deleteAccount(
        @Body body: CommonReqModel<DeleteAccountReqDataModel>,
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<Unit>>

    /**
     * 视频换脸创建任务(New)
     * query: pkg=AppConst.packageName, app="android", userId
     * 入参JSON见 VideoFaceSwapTaskReqDataModel 注释，出参JSON见 VideoFaceSwapTaskRespDataModel 注释
     */
    @POST("/v1/image/video_facewap_task")
    fun createVideoFaceSwapTask(
        @Body body: CommonReqModel<VideoFaceSwapTaskReqDataModel>,
        @Query("userId") userId: String,
        @Query("pkg") pkg: String = AppConst.packageName,
        @Query("app") app: String = AppConst.app,
    ): Call<CommonRespModel<VideoFaceSwapTaskRespDataModel>>

    /**
     * 用户反馈列表
     *
     * "replyStatus" :回复状态，0=未回复，1=已回复，不传则查全部
     *
     */
    @GET("/v1/feedback/list")
    fun getFeedbackList(
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20,
        @Query("replyStatus") replyStatus: Int? = null
    ): Call<CommonRespModel<FeedbackListRespDataModel>>

    /**
     * 提交反馈
     *
     */
    @POST("/v1/feedback/submit")
    fun feedbackSubmit(
        @Body body: CommonReqModel<FeedbackSubmitReqDataModel>,
    ): Call<CommonRespModel<Boolean>>

    /**
     * 用户已读反馈
     *
     */
    @POST("/v1/feedback/read")
    fun feedbackRead(
        @Body body: CommonReqModel<FeedbackReadReqDataModel>,
    ): Call<CommonRespModel<Boolean>>
}