package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 启动事件上报入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/feedback/read",
 *     "enPath": "/fsdgesdga/NC+f8CL4QnvA84Nb4jD1+TtDofgNSm7D",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "jklfijkeijkehijdfghbefgaghicopqkMNOIhijdwxys": [
 *                 "1",
 *                 "2",
 *                 "3"
 *             ] // feedbackIds
 *         }
 *     },
 *     "WARNING": "*****rawPath、enPath、 WARNING作为说明用，不能写到代码里，rawPath是原始path, enPath是混淆后的********",
 *     "vwxrefgarstnhijd4560": "QcA0vfbV" // 固定传参
 */
data class FeedbackReadReqDataModel(
    /**
     * 反馈id列表
     */
    @SerializedName("jklfijkeijkehijdfghbefgaghicopqkMNOIhijdwxys")
    val feedbackIds: List<String> = emptyList()

) : ReqDataInterface {
    override fun getEncKey(): String {
        return "QcA0vfbV"
    }
} 