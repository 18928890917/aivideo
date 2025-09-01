package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取推荐头像列表 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/recommendhead",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4GV7Fw9wUsJMClHAsy4dgaq",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "wxyscdeywxys": [
 *         {
 *           "lmnhijkeefgahijdTUVPstuowxys": "pos", // headPos
 *           "yzauvwxrpqrl": "url", // url
 *           "jklfefgaghicijkeMNOIrstnhijdijkebcdx": 0, // faceIndex
 *           "mnoiqrsmklmg": "img" // img
 *         }
 *       ], // sys
 *       "vwxrijkeghicijkerstnxyzt": [
 *         {
 *           "lmnhijkeefgahijdTUVPstuowxys": "pos", // headPos
 *           "yzauvwxrpqrl": "url", // url
 *           "jklfefgaghicijkeMNOIrstnhijdijkebcdx": 0, // faceIndex
 *           "mnoiqrsmklmg": "img" // img
 *         }
 *       ] // recent
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class RecommendHeadListRespDataModel(
    @SerializedName("wxyscdeywxys")
    val sys: List<HeadInfo>?,
    @SerializedName("vwxrijkeghicijkerstnxyzt")
    val recent: List<HeadInfo>?
) {
    data class HeadInfo(
        @SerializedName("lmnhijkeefgahijdTUVPstuowxys")
        val headPos: String?,
        @SerializedName("yzauvwxrpqrl")
        val url: String?,
        @SerializedName("jklfefgaghicijkeMNOIrstnhijdijkebcdx")
        val faceIndex: Int?,
        @SerializedName("mnoiqrsmklmg")
        val img: String?
    )
} 