package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 换脸生图任务入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/faceswap_task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4H7UgSjhAFy+3JTvQOB16oY",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "lmnhijkeefgahijdTUVPstuowxys": "x1,y1,x2,y2", // headPos
 *             "lmnhijkeefgahijdJKLFefgaghicijkeMNOIrstnhijdijkebcdx": 2, // headFaceIndex
 *             "wxysvwxrghicJKLFefgaghicijkeMNOIrstnhijdijkebcdx": 1, // srcFaceIndex
 *             "lmnhijkeefgahijdMNOIqrsmklmg": "headImg", // headImg
 *             "mnoiqrsmklmg": "img", // img
 *             "vwxrijkewxysstuopqrlyzauxyztmnoistuorstn": "resolution", // resolution
 *             "lmnhijkeefgahijdXYZTcdeytuvpijke": 0 // headType
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "mV0OiJKc" // 固定传参
 * }
 */
data class FaceSwapTaskReqDataModel(
    /**
     * 0 头像用base64编码，
     * 1 推荐头像，headimg内容是路径，通过获取推荐头像列表返回
     */
    @SerializedName("lmnhijkeefgahijdXYZTcdeytuvpijke")
    var headType: Int? = null,

    /**
     * 头像在图片中的坐标，如果自选图片，由脸部信息获取接口返回
     */
    @SerializedName("lmnhijkeefgahijdTUVPstuowxys")
    var headPos: String? = null,
    @SerializedName("lmnhijkeefgahijdJKLFefgaghicijkeMNOIrstnhijdijkebcdx")

    /**
     * 目标脸在图片里面的位置
     *
     */
    var headFaceIndex: Int? = 0,
    @SerializedName("wxysvwxrghicJKLFefgaghicijkeMNOIrstnhijdijkebcdx")

    /**
     * 模版脸在模版图片里面的位置
     *
     */
    var srcFaceIndex: Int? = 0,

    /**
     * 头像
     * 如果是自选的，填入base64，如果是推荐头像，填入推荐头像的路径
     */
    @SerializedName("lmnhijkeefgahijdMNOIqrsmklmg")
    var headImg: String? = null,

    /**
     * 要替换头像的原图，采用base64编码,如果使用模板，填模板名
     */
    @SerializedName("mnoiqrsmklmg")
    var img: String? = null,

    /**
     * img的分辨率， with*height 如：1024*768
     *
     */
    @SerializedName("vwxrijkewxysstuopqrlyzauxyztmnoistuorstn")
    var resolution: String? = null,
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "mV0OiJKc"
    }
} 