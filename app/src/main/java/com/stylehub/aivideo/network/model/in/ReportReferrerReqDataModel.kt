package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface

/**
 * 上报一次归因信息 入参
 * 完整JSON示例：
 * {
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *   "rawPath": "/v1/user/referrer",
 *   "enPath": "/fsdgesdga/ZOosuuxZLKTTYMJ3lViVPO7NoZoquvFR",
 *   "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *       "vwxrijkejklfijkevwxrijkevwxr": "referer", // referer
 *       "hijdijkezabvmnoighicijkeMNOIhijd": "deviceId" // deviceId
 *     }
 *   },
 *   ...
 * }
 */
data class ReportReferrerReqDataModel(
    @SerializedName("vwxrijkejklfijkevwxrijkevwxr")
    val referer: String,
    @SerializedName("hijdijkezabvmnoighicijkeMNOIhijd")
    val deviceId: String
) : ReqDataInterface {
    override fun getEncKey(): String = "EsshcJ8s"
} 