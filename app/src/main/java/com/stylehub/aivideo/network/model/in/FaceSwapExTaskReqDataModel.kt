package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 高级图片换脸入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/faceswap_ex_task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4H7UgSjhAFy+0NHs+0peeL+zAkrC8SJqX0=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "wxysvwxrghicMNOIqrsmklmg5671": "srcImg1", // srcImg1
 *             "wxysvwxrghicMNOIqrsmklmg6782": "srcImg2" // srcImg2
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "tC6EYaGM" // 固定传参
 * }
 */
data class FaceSwapExTaskReqDataModel(
    @SerializedName("wxysvwxrghicMNOIqrsmklmg5671")
    val srcImg1: String,
    @SerializedName("wxysvwxrghicMNOIqrsmklmg6782")
    val srcImg2: String
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "tC6EYaGM"
    }
}
