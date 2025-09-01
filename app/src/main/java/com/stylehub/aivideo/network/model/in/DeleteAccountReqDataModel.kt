package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface

/**
 * 删除账号 入参
 * 完整JSON示例：
 * {
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *   "rawPath": "/v1/user/delete",
 *   "enPath": "/fsdgesdga/ZOosuuxZLKTNQsoM56KIGw==",
 *   "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *       "yzauwxysijkevwxrMNOIhijd": "userId", // userId
 *       "efgatuvptuvp": "app" // app
 *     }
 *   },
 *   ...
 * }
 */
data class DeleteAccountReqDataModel(
    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    val userId: String,
    @SerializedName("efgatuvptuvp")
    val app: String
) : ReqDataInterface {
    override fun getEncKey(): String = "z53fDkHk"
} 