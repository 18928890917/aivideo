package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface

/**
 * 请求创建生成文生图任务 入参
 * 完整JSON示例：
 * {
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *   "rawPath": "/v1/image/txt2img_create",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FkKLMSoeAVoaPDDbhA55JQpLO8Hv2l7tI=",
 *   "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *       "yzauwxysijkevwxrMNOIhijd": "userId", // userId
 *       "efgawxystuvpijkeghicxyztVWXRefgaxyztmnoistuo": "aspectRatio", // aspectRatio
 *       "tuvpvwxrstuoqrsmtuvpxyzt": "prompt", // prompt
 *       "mnoiqrsmklmgGHICstuoyzaurstnxyzt": 1 // imgCount
 *     }
 *   },
 *   ...
 * }
 */
data class Txt2ImgCreateReqDataModel(
    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    val userId: String,
    @SerializedName("efgawxystuvpijkeghicxyztVWXRefgaxyztmnoistuo")
    val aspectRatio: String,
    @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
    val prompt: String,
    @SerializedName("mnoiqrsmklmgGHICstuoyzaurstnxyzt")
    val imgCount: Int
) : ReqDataInterface {
    override fun getEncKey(): String = "bi7IKF79"
} 