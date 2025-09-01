package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 4.请求创建换衣生图任务
 *
 * 用于 /v1/image/clothes_swap_ex 接口的入参
 *
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/clothes_swap_ex",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4H2BFsoPdksNThMEpISIDUqvfFcTnBpvZs=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904": "srcImgBase64" // srcImgBase64
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "pbxU5SF8" // 固定传参
 * }
 */
data class ClothesSwapExReqDataModel(
    @SerializedName("wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904")
    var srcImgBase64: String? = null
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "pbxU5SF8"
    }
} 