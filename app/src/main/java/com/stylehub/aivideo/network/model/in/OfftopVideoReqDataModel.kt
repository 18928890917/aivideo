package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 图片动作风格化gif入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/offtop_video",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4GZkgR+UpnK_zW+uc2XbwwF",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmg": "srcImg", // srcImg
 *             "zabvmnoihijdijkestuoWXYSmnoidefzijke": "480p" // videoSize
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "gKr0jbmQ" // 固定传参
 * }
 */
data class OfftopVideoReqDataModel(
    @SerializedName("wxysvwxrghicMNOIqrsmklmg")
    val srcImg: String,
    @SerializedName("zabvmnoihijdijkestuoWXYSmnoidefzijke")
    val videoSize: String
) : ReqDataInterface {
    override fun getEncKey(): String = "gKr0jbmQ"
} 