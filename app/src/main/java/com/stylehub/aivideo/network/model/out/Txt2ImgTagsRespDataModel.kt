package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 文生图tag模板 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/txt2img_tags",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FkKLMSoeAVoc+YG6pZ98xH",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "xyztefgaklmgwxys": [
 *           {
 *             "hijdijkewxysghic": "desc", // desc
 *             "xyztefgaklmg": "tag" // tag
 *           }
 *         ], // tags
 *         "xyztlmnhijkeqrsmijke": "theme" // theme
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class Txt2ImgTagsRespDataModel(
    @SerializedName("xyztefgaklmgwxys")
    val tags: List<Tag>?,
    @SerializedName("xyztlmnhijkeqrsmijke")
    val theme: String?
) {
    data class Tag(
        @SerializedName("hijdijkewxysghic")
        val desc: String?,
        @SerializedName("xyztefgaklmg")
        val tag: String?
    )
} 