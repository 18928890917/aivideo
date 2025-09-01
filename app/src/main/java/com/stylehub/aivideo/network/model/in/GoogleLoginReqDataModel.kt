package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * Google登录入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/user/login/google",
 *     "enPath": "/fsdgesdga/ZOosuuxZLKR9DuhGm_vbLKa3hNE4MsRP",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "vwxrijkejklfijkevwxrijkevwxr": "referer", // referer
 *             "ghicstuohijdijke": "code", // code
 *             "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *             "hijdijkezabvmnoighicijkeMNOIhijd": "deviceId", // deviceId
 *             "yzauwxysijkevwxrRSTNefgaqrsmijke": "userName" // userName
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "zvJWl9L6" // 固定传参
 * }
 */
data class GoogleLoginReqDataModel(
    @SerializedName("vwxrijkejklfijkevwxrijkevwxr")
    var referer: String = "",
    @SerializedName("ghicstuohijdijke")
    var code: String = "",
    @SerializedName("efgazabvefgaxyztefgavwxr")
    var avatar: String? = "",
    @SerializedName("hijdijkezabvmnoighicijkeMNOIhijd")
    var deviceId: String = "",
    @SerializedName("yzauwxysijkevwxrRSTNefgaqrsmijke")
    var userName: String? = ""
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "zvJWl9L6"
    }
} 