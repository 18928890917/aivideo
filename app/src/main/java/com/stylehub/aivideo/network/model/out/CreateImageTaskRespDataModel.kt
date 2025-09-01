package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 通用图生任务出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/create-task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4HwSk6p_bz8R8m+1w7CqCny",
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
class CreateImageTaskRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null
} 