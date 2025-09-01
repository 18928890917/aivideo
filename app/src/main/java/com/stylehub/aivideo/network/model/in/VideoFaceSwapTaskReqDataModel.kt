package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface

/**
 * 视频换脸创建任务(New) 入参
 * 完整JSON示例：
 * {
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *   "rawPath": "/v1/image/video_facewap_task",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4GDi1ueBNb7goX2EHMPMFxChnJqh7czAxo=",
 *   "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *       "lmnhijkeefgahijdTUVPstuowxys": "headPos", // headPos
 *       "zabvmnoihijdijkestuoJKLFefgaghicijkeMNOIrstnhijdijkebcdx": 1, // videoFaceIndex
 *       "lmnhijkeefgahijdJKLFefgaghicijkeMNOIrstnhijdijkebcdx": 3, // headFaceIndex
 *       "lmnhijkeefgahijdMNOIqrsmklmg": "headImg", // headImg
 *       "xyztcdeytuvpijke": 0, // type
 *       "zabvmnoihijdijkestuoJKLFvwxrefgaqrsmijkeMNOIrstnhijdijkebcdx": 10, // videoFrameIndex
 *       "zabvmnoihijdijkestuoYZAUvwxrpqrl": "videoUrl", // videoUrl
 *       "lmnhijkeefgahijdXYZTcdeytuvpijke": 2, // headType
 *       "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName" // templateName
 *     }
 *   },
 *   ...
 * }
 */
data class VideoFaceSwapTaskReqDataModel(

    /**
     * 头像在图片中的坐标，由脸部信息获取接口返回。
     *
     */
    @SerializedName("lmnhijkeefgahijdTUVPstuowxys")
    var headPos: String? = null,

    /**
     * 要替换人物头像在视频中的帧的头像位置，模板方式可不填
     *
     */
    @SerializedName("zabvmnoihijdijkestuoJKLFefgaghicijkeMNOIrstnhijdijkebcdx")
    var videoFaceIndex: Int? = null,

    /**
     * 头像的在图中的位置
     *
     */
    @SerializedName("lmnhijkeefgahijdJKLFefgaghicijkeMNOIrstnhijdijkebcdx")
    var headFaceIndex: Int? = 0,

    /**
     * 头像，根据headType写入
     */
    @SerializedName("lmnhijkeefgahijdMNOIqrsmklmg")
    var headImg: String? = null,

    /**
     * 0 视频模板方式， 1 自定义上传视频
     */
    @SerializedName("xyztcdeytuvpijke")
    var type: Int? = 0,

    /**
     * 要替换人物头像在视频中的帧数，模板方式可不填
     *
     */
    @SerializedName("zabvmnoihijdijkestuoJKLFvwxrefgaqrsmijkeMNOIrstnhijdijkebcdx")
    var videoFrameIndex: Int? = null,

    /**
     * 自定义视频上传后返回的URL", //接口(视频信息获取)返回的videoUrl， 模板方式可不填
     *
     */
    @SerializedName("zabvmnoihijdijkestuoYZAUvwxrpqrl")
    var videoUrl: String? = null,

    /**
     * 0 headimg内容base64编码，1 推荐头像，headimg内容是路径，通过获取推荐头像列表返回 （img 字段）
     *
     */
    @SerializedName("lmnhijkeefgahijdXYZTcdeytuvpijke")
    var headType: Int? = null,

    /**
     * "视频模板名", //自定义可留空
     *
     */
    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke")
    var templateName: String? = null,
) : ReqDataInterface {
    override fun getEncKey(): String = "R7FFyOv7"
} 