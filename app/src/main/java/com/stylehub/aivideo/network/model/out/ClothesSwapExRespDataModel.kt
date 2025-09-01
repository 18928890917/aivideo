package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 4.请求创建换衣生图任务出参
 *
 * 用于 /v1/image/clothes_swap_ex 接口的返回数据
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
class ClothesSwapExRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null
} 