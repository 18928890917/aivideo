package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 自定义换衣出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/custom_clothes_swap",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4G+09O1FmvDqAeH4ps9+Uyoh2R+hRreiLU=",
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
class CustomClothesSwapRespDataModel {
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null
} 