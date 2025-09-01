package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 *
 * Create by league at 2025/6/24
 *
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/user/fast_login",
 *     "enPath": "/fsdgesdga/ZOosuuxZLKR8Gd8KHwyk_zvfNoWpSjEI",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "vwxrijkejklfijkevwxrijkevwxr": "referer", // referer
 *             "wxysmnoiklmgrstn": "sign", // sign
 *             "hijdijkezabvmnoighicijkeMNOIhijd": "deviceId" // deviceId
 *         }
 *     },
 *     "WARNING": "*****rawPath、enPath、 WARNING作为说明用，不能写到代码里，rawPath是原始path, enPath是混淆后的********",
 *     "vwxrefgarstnhijd4560": "buzlBgPT" // 固定传参
 * }
 */
data class FastLoginReqDataModel(
    @SerializedName("vwxrijkejklfijkevwxrijkevwxr")
    val referer: String,

    @SerializedName("wxysmnoiklmgrstn")
    val sign: String,

    @SerializedName("hijdijkezabvmnoighicijkeMNOIhijd")
    val deviceId: String

) : ReqDataInterface {
    override fun getEncKey(): String {
        return "buzlBgPT"
    }
}
