package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 请求创建黏土风格图片任务出参
 *
 * 用于 /v1/image/clay_stylization 接口的返回数据
 *
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
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
class ClayStylizationRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null
} 