package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import kotlin.math.max

/**
 * 图片生成视频post模版出参
 *
 * 完整 JSON：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/img2Video_pose_template",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4Gy9GvMC5N3jnVX9LE8e+yorE9aoxy1BKV1q+Gml8I4UQ==",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "yzauwxysijkevwxr": "user", // user
 *         "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "templateUrl", // templateUrl
 *         "hijdyzauvwxrefgaxyztmnoistuorstn": 12, // duration
 *         "ghicvwxrijkehijdmnoixyztwxys": {
 *           "56719015": 5, // 15
 *           "67829015": 10 // 25
 *         }, // credits
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName", // templateName
 *         "xyztefgaklmg": "tag" // tag
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 *
 *
 * 实际示例（仅data内容）：
 * {
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFmnoijklfHIJDyzauvwxrefgaxyztmnoistuorstn": 3,
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFmnoijklfGHICvwxrijkehijdmnoixyztwxys": 8,
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeXYZTjklfYZAUvwxrpqrl": "hormony/template_video/poseTemplate/template1-3-25.mp4",
 *     "efgarstnmnoiqrsmefgaxyztijke": "https://storage.googleapis.com/hormonai-cdn/hormony/template_video/animate/template1-3.gif?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T072710Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003d6ee94e929911ad9403cef61d242304125e72488caf7015f8599fa60dca218c1f2dabcdc596027754fdbdc057ea9587445e31c6fb40113b8ee4b5041b34ea96c33fb9c18b5cb6abbfba68af448dccc06781051e421949d18e179c95d958926579025a5fae9599c9e1d24ffe9ea55e00669a9e94b40c40a1ce119cc05d74be01a5647438950339e8e1a8fd19b0e7d7c97575f2ac853f607097320e5ea33ac85d8019ab3c7b6f4442ad59d5e86fc25211b5a03c64155d4ef7a68d4e341ae2dc86b3f913533fb500fbe5c442ca70838431a392d0203028c1dd8805cdfbb4c86eaef201a376886d7584ecae630a219a07d6c79a1151d4d0a1ccd08197395b4f3c3fe4",
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "https://storage.googleapis.com/hormonai-cdn/hormony/template_video/poseTemplate/template1-3-25.mp4?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T072710Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003d010372f5b479449f346aed4d9139a604b126078f40d392e4247d07a015235355547c38e6661ad6fd8dbc72c139b36999bc1264aa33c38dafe0d786035e74b13c7a136c1b7d9d178b12a6012455b10efcef616fe27845183670192424b4e83381e2e23d6d4450f847e7ce8c0e84edb8deb4a6617e4b2e1f37e2e14b21beef1ab1edda663fe843394010354d18094bb6dab926735b86f3f5715cde6346d87f23b66edf91ef510d7b11871a412d8bc6058658c79b589e8cfe6b0985141d8b4cf8c42d303204533a6a9a84d2a48aca2f1c51075e741c11d6c86adfbaf87e6c3c45b7218a765a8a09505893c0cfd747b8129a945fb643f35d9e62c571e01e7272d443",
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeXYZTjklfGHICvwxrijkehijdmnoixyztwxys": 12,
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFmnoijklfYZAUvwxrpqrl": "hormony/template_video/poseTemplate/template1-3-15.mp4",
 *     "hijdyzauvwxrefgaxyztmnoistuorstn": 3,
 *     "ghicvwxrijkehijdmnoixyztwxys": {
 *       "56719015": 8,
 *       "67829015": 12
 *     },
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeXYZTjklfHIJDyzauvwxrefgaxyztmnoistuorstn": 3,
 *     "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "template1_A"
 * }
 *
 */
class GetImg2VideoPoseTemplateRespDataModel : Serializable, Template{
    @SerializedName("yzauwxysijkevwxr")
    // user
    val user: String? = null

    @SerializedName("efgazabvefgaxyztefgavwxr")
    // avatar
    val avatar: String? = null

    /**
     * 模板地址路径
     */
    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFmnoijklfYZAUvwxrpqrl")
    val templatePath: String? = null

    /**
     * 模板地址url（视频）
     */
    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl")
    // templateUrl
    val templateUrl: String? = null

    /**
     * 预览图(gif)
     */
    @SerializedName("efgarstnmnoiqrsmefgaxyztijke")
    val animate: String? = null

    /**
     * 视频时长
     */
    @SerializedName("hijdyzauvwxrefgaxyztmnoistuorstn")
    // duration
    val duration: Int? = null

    /**
     * 这里的key代表的是帧率，value代表的是该帧率的价格点数
     */
    @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
    // credits
    val credits: Map<String, Int>? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke")
    // templateName
    val templateName: String? = null

    @SerializedName("xyztefgaklmg")
    // tag
    val tag: String? = null

    fun getCredit(): Int {

        var credit = 1
        credits?.values?.forEach {
            credit = max(credit, it)
        }
        return credit
    }

    override fun getTmpCredits(): Int {
        return getCredit()
    }

    override fun getTmpName(): String? {
        return templateName
    }

    override fun getTmpUrl(): String? {
        return templateUrl
    }

    override fun getPreviewUrl(): String? {
        return animate
    }
} 