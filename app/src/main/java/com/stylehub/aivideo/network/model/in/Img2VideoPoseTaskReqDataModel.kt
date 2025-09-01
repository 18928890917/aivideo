package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 图片生成视频pose入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/img2video_pose_task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4FfpY18VoI6AHVX9LE8e+yoNhNgMXLDM6g=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904": "srcImgBase64", // srcImgBase64
 *             "wxysvwxrghicZABVmnoihijdijkestuo": "srcVideo" // srcVideo
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "HUtfQ8H4" // 固定传参
 * }
 *
 * 注意：接口已改为form-data方式，调用时请用RequestBody封装base64字符串
 */
class Img2VideoPoseTaskReqDataModel : ReqDataInterface {

    @SerializedName("wxysvwxrghicMNOIqrsmklmgFGHBefgawxysijke01268904")
    var srcImgBase64: String? = null

    @SerializedName("wxysvwxrghicZABVmnoihijdijkestuo")
    var srcVideo: String? = null

    override fun getEncKey(): String {
        return "HUtfQ8H4"
    }
} 