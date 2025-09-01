package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 7.视频换脸模版出参
 *
 * 完整 JSON：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/faceswap_video_template",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4H7UgSjhAFy+9PDtr2Q8gn8rE9aoxy1BKV1q+Gml8I4UQ==",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "yzauwxysijkevwxr": "user", // user
 *         "efgarstnmnoiqrsmefgaxyztijke": "animate", // animate
 *         "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "templateUrl", // templateUrl
 *         "jklfefgaghicijkeMNOIrstnhijdijkebcdx": 0, // faceIndex
 *         "jklfvwxrefgaqrsmijkeMNOIrstnhijdijkebcdx": 1, // frameIndex
 *         "ghicvwxrijkehijdmnoixyztwxys": 11, // credits
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFefgaghicijkeYZAUvwxrpqrl": "templateFaceUrl", // templateFaceUrl
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName", // templateName
 *         "xyztefgaklmg": "tag" // tag
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 *
 * 实际数据：
 *
 * {
 *      "yzauwxysijkevwxr": "منويلا2",
 *      "efgarstnmnoiqrsmefgaxyztijke": "https://storage.googleapis.com/hormonai-cdn/hormony/template_video/aimaker_video_01_short.gif?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T110901Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003d2eb85f231476c3f004b6d3d59a3168c5ab5fcdb1815035207b87ca9e21e5829e3a2bc602c4b74b36b9f4ba69680d558bb2e648a9c4cab04da0f0085360eccc48841d990c3660e737b0cbb5e5914e3a5302f957003b4aeb337b37e759b889e4205fad6b6b579245f97e21458d73fee1e8602f5b30dc96457b9f2af6891e3ab8188754f21c1cd0b1fa0ac91a859cf5850c4b1ffd8991658d386495fa55c97c7357a62b361c0ab6d6c853bb2d946294717917bcc81f90ad2a765ba811087e13967ecd29c24a83139fb71bf33d64316f9c74fab89bd1ce2866a8a12bc5206a135994256e22a410c9c03486f3ff40c1fcc35f35a2e3a6eba1c348b2ebc92cb524ca95",
 *      "efgazabvefgaxyztefgavwxr": "https://storage.googleapis.com/hormonai-cdn/hormony/avatar/20240202/1706811675459.jpeg?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T110901Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003d7b953783eca68b55f49142871d2345199d9194729f47a504ca38408fc6c4a1522e6ec18a344ef98d2a3ccf2168fa33e34e7d3f690ec13509e564623f945ef82e6f0c784a01d8f6ed5fd19d4814a8e2d1ac207af90d4f259ccf2aa2e099da0e96cf9238d62a1aaf6f50bca66da7a77224d1c5fd33d23c0366b109320b4d151d65ac6dc656df294c6bf3a825fec95edaecda487e92e9f71e96b5a5009ef4f27ac257201bc4bdae474d8a14cdbee6fb6d0e74e141a26df322f6fc722be1a48e6d957131cf09d1c69acb62131b1d77085ed2c49d86161afabdf6f430020ae9db491b3c0112cdbd5dfefc60f5a9273e70f2c5ac83023bb89f0e8a10791486c29ea7f3",
 *      "xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl": "https://storage.googleapis.com/hormonai-cdn/hormony/template_video/aimaker_video_01.mp4?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T110901Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003db5eb09f09509c949933aded7b49b35d05fc34d304288f2efa6e27298d3972e204a290a06270977fbb4e5ac44dd502f12b2f82bc3d0c44153de8fc4970d72038c31e98406579387693fb5ff7837bee540e0127b6c8b4a2a3925fcdf3ed6273eaf66b44344d94410aa1261b78384893d1a70b5ff1fb3ffec8dad7f87359b88adb185c946a9842ad26c6d5a3e6b3b2c5bfe7c1445948c661766f05c316793afc2dbd59473bb49016d3a96598899839f619579fabae94f23d978065b1a5f68e73499da9bbfcffbb8e7259eecb6b13ef78b7daf94d7cf49d5b351416c3907ad943e2c54451e7a712b84d517c1b5fc5ec14553f2649263063522653d3a02ac0236af3b",
 *      "jklfefgaghicijkeMNOIrstnhijdijkebcdx": 0,
 *      "jklfvwxrefgaqrsmijkeMNOIrstnhijdijkebcdx": 450,
 *      "zabvmnoihijdijkestuoHIJDyzauvwxrefgaxyztmnoistuorstn": 15,
 *      "ghicvwxrijkehijdmnoixyztwxys": 15,
 *      "xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFefgaghicijkeYZAUvwxrpqrl": "https://storage.googleapis.com/hormonai-cdn/hormony/template_video/aimaker_video_01_face.jpg?X-Goog-Algorithm\u003dGOOG4-RSA-SHA256\u0026X-Goog-Credential\u003dhl-project-napay%40project-napay.iam.gserviceaccount.com%2F20250705%2Fauto%2Fstorage%2Fgoog4_request\u0026X-Goog-Date\u003d20250705T110901Z\u0026X-Goog-Expires\u003d600\u0026X-Goog-SignedHeaders\u003dhost\u0026X-Goog-Signature\u003d9a1a6311755cbcb13a17a8e326a61ff44cfad530fe27a3f84602e0992b761a24e5e46529d173f9a90bf028607d11288362c17dfd90743c9bedef19af9f36e4ebec71a75da994c56474c6bfc42789d792ae6c4bf87866eb9c1d0dc6bb0a54aa92fe9ced889b56f669c9b58b32355619ac98dbf18c90059b70f3aad789938ce7f75780ea870cd8d18d3fec5c441a334920b6cc6d64a53114413da2b70d7d8995a9ee1008461aecd4971a889aca77ad5c18a9e16ab4ef619d1a5158aef5fa31d4d90c39be9f0d0838b92324cfb86d08d50ebf114b97effe148079aeef25d35c9a7de1380262d0b75d4e62b8ada083b699149e9eec65d27e8a5e775fbf50a3582049",
 *      "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "template_aimaker_1_A"
 * }
 */
class FaceSwapVideoTemplateRespDataModel: Template, Serializable {
    @SerializedName("yzauwxysijkevwxr")
    // user
    var user: String? = null

    @SerializedName("efgarstnmnoiqrsmefgaxyztijke")
    // animate
    var animate: String? = null

    @SerializedName("efgazabvefgaxyztefgavwxr")
    // avatar
    var avatar: String? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeYZAUvwxrpqrl")
    // templateUrl
    var templateUrl: String? = null

    @SerializedName("jklfefgaghicijkeMNOIrstnhijdijkebcdx")
    // faceIndex
    var faceIndex: Int? = null

    @SerializedName("jklfvwxrefgaqrsmijkeMNOIrstnhijdijkebcdx")
    // frameIndex
    var frameIndex: Int? = null

    @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
    // credits
    var credits: Int? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeJKLFefgaghicijkeYZAUvwxrpqrl")
    // templateFaceUrl
    var templateFaceUrl: String? = null

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
        return credits?:1
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