package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 视频信息获取（上传视频）入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/get_video_faces",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4GIjfSvLt6jspikzW8+b89+yBdWGq7Y7bw=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "hijdyzauvwxrefgaxyztmnoistuorstn": 12, // duration
 *             "wxysvwxrghicZABVmnoihijdijkestuo": "srcVideo" // srcVideo
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "TiAHGdoe" // 固定传参
 * }
 */
data class GetVideoFacesReqDataModel(
    @SerializedName("hijdyzauvwxrefgaxyztmnoistuorstn")
    val duration: Int,
    @SerializedName("wxysvwxrghicZABVmnoihijdijkestuo")
    val srcVideo: String
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "TiAHGdoe"
    }
} 