package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 请求创建黏土风格图片任务
 *
 * 用于 /v1/image/clay_stylization 接口的入参
 *
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/clay_stylization",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4G8oLm81CEvAuO2ByLsZPYCnTSQ17oA1uU=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904": "srcImgBase64" // srcImgBase64
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "HqNmTiPc" // 固定传参
 * }
 */
data class ClayStylizationReqDataModel(
    @SerializedName("wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904")
    var srcImgBase64: String? = null
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "HqNmTiPc"
    }
} 