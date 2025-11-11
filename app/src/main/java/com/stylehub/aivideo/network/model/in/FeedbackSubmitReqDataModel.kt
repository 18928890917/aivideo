package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 启动事件上报入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/feedback/submit",
 *     "enPath": "/fsdgesdga/NC+f8CL4QnsqAkmxcuEv0zBn1ozHEQ8w",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "ghicstuorstnxyztijkerstnxyzt": "hello", // content
 *             "jklfmnoipqrlijkeYZAUvwxrpqrlwxys": [
 *                 "cc185a05c2.jpg"
 *             ] // fileUrls
 *         }
 *     },
 *     "WARNING": "*****rawPath、enPath、 WARNING作为说明用，不能写到代码里，rawPath是原始path, enPath是混淆后的********",
 *     "vwxrefgarstnhijd4560": "3BcXRKGG" // 固定传参
 * }
 */
data class FeedbackSubmitReqDataModel(

    /**
     * 反馈内容
     */
    @SerializedName("ghicstuorstnxyztijkerstnxyzt")
    val content: String? = null,

    /**
     * 图片列表
     */
    @SerializedName("jklfmnoipqrlijkeYZAUvwxrpqrlwxys")
    val fileUrls: List<String>? = null

) : ReqDataInterface {
    override fun getEncKey(): String {
        return "3BcXRKGG"
    }
} 