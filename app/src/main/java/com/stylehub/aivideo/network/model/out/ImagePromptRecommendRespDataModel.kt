package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取文生图推荐提示 出参
 * 完整JSON示例：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/prompt/recomends",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4FRDZGUaSCs9Lmb0myRZb80KHaV7s56oF0=",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *             {
 *                 "tuvpvwxrstuoqrsmtuvpxyzt": "prompt", // prompt
 *                 "xyztefgaklmg": "tag" // tag
 *             }
 *         ]
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     "vwxrefgarstnhijd6782": "fr2v63eb", // 固定传参
 *     "vwxrefgarstnhijd7893": "jwxLPqJe", // 固定传参
 *     "vwxrefgarstnhijd5671": "E6FEK4Ju", // 固定传参
 *     "vwxrefgarstnhijd4560": "7Q8yGPsz" // 固定传参
 * }
 */
data class ImagePromptRecommendRespDataModel(
    @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
    val prompt: String?,
    @SerializedName("xyztefgaklmg")
    val tag: String?
) 