package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 文生图提示语润色接口 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/txt2img_prompts",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FkKLMSoeAVodREDWPhuvPoyBdWGq7Y7bw=",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "xyztefgaklmgwxys": "tags", // tags
 *       "tuvpvwxrstuoqrsmtuvpxyzt": "prompt" // prompt
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class Txt2ImgPromptsRespDataModel(
    @SerializedName("xyztefgaklmgwxys")
    val tags: String?,
    @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
    val prompt: String?
) 