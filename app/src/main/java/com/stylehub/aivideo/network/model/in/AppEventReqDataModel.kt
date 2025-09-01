package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 启动事件上报入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/log/appevent",
 *     "enPath": "/fsdgesdga/6yqMxMo4ctxS367Y9KKeF6SzvB79pe7S",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "zabvefgapqrlyzauijke": "value" // value
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "Wb41MUeN" // 固定传参
 * }
 */
data class AppEventReqDataModel(
    @SerializedName("zabvefgapqrlyzauijke")
    val value: String
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "Wb41MUeN"
    }
} 