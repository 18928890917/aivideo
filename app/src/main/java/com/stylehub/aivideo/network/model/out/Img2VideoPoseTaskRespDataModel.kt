package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 图片生成视频pose出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/img2video_pose_task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4FfpY18VoI6AHVX9LE8e+yoNhNgMXLDM6g=",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "wxysxyztefgaxyztijke": 0, // state
 *             "xyztefgawxysopqkMNOIhijd": 23123 // taskId
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     ...
 * }
 */
class Img2VideoPoseTaskRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null
} 