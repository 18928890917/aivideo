package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 高级图片换脸出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/faceswap_ex_task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4H7UgSjhAFy+0NHs+0peeL+zAkrC8SJqX0=",
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
class FaceSwapExTaskRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Int? = null
} 