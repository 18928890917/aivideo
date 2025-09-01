package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface

/**
 * 文生图提示语润色接口 入参
 * 完整JSON示例：
 * {
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *   "rawPath": "/v1/image/txt2img_prompts",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FkKLMSoeAVodREDWPhuvPoyBdWGq7Y7bw=",
 *   "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *       "hijdefgaxyztefga": [
 *         {
 *           "xyztefgaklmgwxys": [
 *             "tag"
 *           ], // tags
 *           "ghicyzauwxysxyztstuoqrsm": "custom", // custom
 *           "xyztlmnhijkeqrsmijke": "theme" // theme
 *         }
 *       ] // data
 *     }
 *   },
 *   ...
 * }
 */
data class Txt2ImgPromptsReqDataModel(
    @SerializedName("hijdefgaxyztefga")
    val data: List<DataItem>
) : ReqDataInterface {
    override fun getEncKey(): String = "bXU3JlAu"
    data class DataItem(
        @SerializedName("xyztefgaklmgwxys")
        val tags: List<String>,
        @SerializedName("ghicyzauwxysxyztstuoqrsm")
        val custom: String,
        @SerializedName("xyztlmnhijkeqrsmijke")
        val theme: String
    )
} 