package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 自定义换衣入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/custom_clothes_swap",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4G+09O1FmvDqAeH4ps9+Uyoh2R+hRreiLU=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmg5671": "srcImg1", // srcImg1
 *             "wxysvwxrghicMNOIqrsmklmg6782": "srcImg2" // srcImg2
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "lHLKJjmW" // 固定传参
 * }
 */
data class CustomClothesSwapReqDataModel(

    /**
     * 原图 base64图片数据
     *
     */
    @SerializedName("wxysvwxrghicMNOIqrsmklmg5671")
    var srcImg1: String? = null,

    /**
     * 衣服图片 base64数据 （可以是穿衣服的人物图）
     *
     */
    @SerializedName("wxysvwxrghicMNOIqrsmklmg6782")
    var srcImg2: String? = null,
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "lHLKJjmW"
    }
} 