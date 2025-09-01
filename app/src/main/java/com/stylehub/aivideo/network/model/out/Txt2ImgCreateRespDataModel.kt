package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 请求创建生成文生图任务 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/txt2img_create",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FkKLMSoeAVoaPDDbhA55JQpLO8Hv2l7tI=",
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
data class Txt2ImgCreateRespDataModel(
    @SerializedName("wxysxyztefgaxyztijke")
    val state: Int?,
    @SerializedName("xyztefgawxysopqkMNOIhijd")
    val taskId: Long?
) 