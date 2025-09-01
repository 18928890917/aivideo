package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 脸部信息获取入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/get_image_faces",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4HjvZcx3bXTfl2QHNx6hMcGyBdWGq7Y7bw=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904": "srcImgBase64" // srcImgBase64
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "tG7zIZ3f" // 固定传参
 * }
 */
data class GetImageFacesReqDataModel(
    @SerializedName("wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904")
    val srcImgBase64: String
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "tG7zIZ3f"
    }
} 