package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 获取推荐图片模板(图片换脸模板)出参
 *
 * 完整 JSON：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/image_template",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4H59qdSvY3lLgUWMZGpolGfpLO8Hv2l7tI=",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "lmnhijkeefgahijdTUVPstuowxys": "headPos", // headPos
 *         "yzauwxysijkevwxr": "user", // user
 *         "vwxrijkewxysyzaupqrlstuoxyztmnoistuorstn": "resulotion", // resulotion
 *         "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "templateUrl", // templateUrl
 *         "jklfefgaghicijkeMNOIrstnhijdijkebcdx": 0, // faceIndex
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName", // templateName
 *         "xyztefgaklmg": "tag" // tag
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
class GetImageTemplateRespDataModel: Template, Serializable {
    @SerializedName("lmnhijkeefgahijdTUVPstuowxys")
    // headPos
    var headPos: String? = null

    @SerializedName("yzauwxysijkevwxr")
    // user
    var user: String? = null

    @SerializedName("vwxrijkewxysyzaupqrlstuoxyztmnoistuorstn")
    // resulotion
    var resulotion: String? = null

    @SerializedName("efgazabvefgaxyztefgavwxr")
    // avatar
    var avatar: String? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl")
    // templateUrl
    var templateUrl: String? = null

    @SerializedName("jklfefgaghicijkeMNOIrstnhijdijkebcdx")
    // faceIndex
    var faceIndex: Int? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke")
    // templateName
    var templateName: String? = null

    @SerializedName("xyztefgaklmg")
    // tag
    var tag: String? = null

    /**
     * 没有点数默认为1
     */
    override fun getTmpCredits(): Int {
        return 1
    }

    override fun getTmpName(): String? {
        return templateName
    }

    override fun getTmpUrl(): String? {
        return templateUrl
    }
} 