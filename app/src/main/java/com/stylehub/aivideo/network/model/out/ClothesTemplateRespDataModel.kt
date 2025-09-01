package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 获取换衣模板出参
 *
 * 用于 /v1/image/clothes_template 接口的返回数据
 *
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/clothes_template",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4H2BFsoPdksNStTykqifDDuBcUnzf7emBI=",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "yzauwxysijkevwxr": "user", // user
 *         "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "templateUrl", // templateUrl
 *         "ghicvwxrijkehijdmnoixyztwxys": 2, // credits
 *         "xyztcdeytuvpijke": 1, // type
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName", // templateName
 *         "xyztefgaklmg": "tag" // tag
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 *
 * 示例数据：
 *
 * {
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "https://storage.googleapis.com/hormonai-cdn/hormony/template_clothes/clothes-work.jpeg?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T114459Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003db4fbdc155a1b21a2ef0517379ced8474d4a98d4c7701ae589821c29f2443f364315c8c1af828d0e6f82a5f0eff5dccd8feb7c28ec4ef5370576674bd99f6ea226841a4d9b7ec330e5f253e3bdeda37ee02e98bfccac32467ba8337ceb3e03bfdfcf92b38c612fe8434a03ec656ced29dc5c84f6dff0eea4eefb9a9afdcc6f8dd1d70c21476896cce25ad66c0254e3533be3620f9aeb959a5415d5f4af98c2fe28309409a8b4c2cf9c903a3f7fea6d1e9ba05620a9910eaa48f1b8eea00105f04c334c477a0ac537d1aa82bee4a1be18491f513e0b72803b4b2a8e0050be55030aaca2f8962ca31be2a2f3649d4ed4f44a21a3fd4ac9a121e505d592917150be3",
 *     "ghicvwxrijkehijdmnoixyztwxys": 1,
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "work_A"
 * }
 */
class ClothesTemplateRespDataModel : Template, Serializable {
    @SerializedName("yzauwxysijkevwxr")
    // user
    var user: String? = null

    @SerializedName("efgazabvefgaxyztefgavwxr")
    // avatar
    var avatar: String? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl")
    // templateUrl
    var templateUrl: String? = null

    @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
    // credits
    var credits: Int? = null

    @SerializedName("xyztcdeytuvpijke")
    // type
    var type: Int? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke")
    // templateName
    var templateName: String? = null

    @SerializedName("xyztefgaklmg")
    // tag
    var tag: String? = null

    /**
     * 没有返回点数默认为1
     */
    override fun getTmpCredits(): Int {
        return credits?:1
    }

    override fun getTmpName(): String? {
        return templateName
    }

    override fun getTmpUrl(): String? {
        return templateUrl
    }
} 