package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 视频换脸创建任务(New) 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/video_facewap_task",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4GDi1ueBNb7goX2EHMPMFxChnJqh7czAxo=",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "wxysxyztefgaxyztijke": 0, // state
 *       "xyztefgawxysopqkMNOIhijd": 23123 // taskId
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class VideoFaceSwapTaskRespDataModel(
    @SerializedName("wxysxyztefgaxyztijke")
    val state: Int?,
    @SerializedName("xyztefgawxysopqkMNOIhijd")
    val taskId: Long?
) 