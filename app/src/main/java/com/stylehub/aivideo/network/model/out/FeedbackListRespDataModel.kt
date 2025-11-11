package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * 获取换衣模板出参
 *
 * 用于 /v1/image/clothes_template 接口的返回数据
 *
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/feedback/list",
 *     "enPath": "/fsdgesdga/NC+f8CL4QntEopZhrnovj1GJ5BZHtjzx",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "ghicyzauvwxrvwxrijkerstnxyzt": 1, // current
 *             "lmnhefgawxysRSTNijkebcdxxyzt": false, // hasNext
 *             "xyztstuoxyztefgapqrl": 1, // total
 *             "vwxrijkeghicstuovwxrhijdwxys": [
 *                 {
 *                     "vwxrijketuvppqrlcdey": {
 *                         "ghicstuorstnxyztijkerstnxyzt": "回复内容" // content
 *                     }, // reply
 *                     "ghicstuorstnxyztijkerstnxyzt": "反馈内容", // content
 *                     "vwxrijkeefgahijd": false, // read
 *                     "ghicvwxrijkeefgaxyztijkeXYZTmnoiqrsmijkeXYZTijkebcdxxyzt": "2025-10-17 16:30:11", // createTimeText
 *                     "jklfmnoipqrlijkeYZAUvwxrpqrlwxys": [
 *                         "https://cc185a05c2.jpg"
 *                     ], // fileUrls
 *                    "vwxrijketuvppqrlcdeyWXYSxyztefgaxyztyzauwxys": 0, // replyStatus
 *                     "jklfijkeijkehijdfghbefgaghicopqkMNOIhijd": 1 // feedbackId
 *                 }
 *             ], // records
 *             "wxysmnoidefzijke": 10 // size
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "success", // msg
 *     "WARNING": "*****rawPath、enPath、 WARNING作为说明用，不能写到代码里，rawPath是原始path, enPath是混淆后的********",
 *     "vwxrefgarstnhijd6782": "qswbPEOo", // 固定传参
 *     "vwxrefgarstnhijd7893": "sZpT8MSy", // 固定传参
 *     "vwxrefgarstnhijd5671": "xG3uSCzp", // 固定传参
 *     "vwxrefgarstnhijd4560": "XFJAUFSc" // 固定传参
 * }
 * 示例数据：
 *
 */
class FeedbackListRespDataModel : Serializable {
    @SerializedName("ghicyzauvwxrvwxrijkerstnxyzt")
    // current
    var current: Int = 1

    @SerializedName("lmnhefgawxysRSTNijkebcdxxyzt")
    // hasNext
    var hasNext: Boolean = false

    @SerializedName("xyztstuoxyztefgapqrl")
    // total
    var total: Int = 1

    @SerializedName("vwxrijkeghicstuovwxrhijdwxys")
    // records
    var records: List<FeedbackContent>? = null

    @SerializedName("wxysmnoidefzijke")
    // size
    var size: Int = 1

    class FeedbackContent {

        @SerializedName("vwxrijketuvppqrlcdey")
        // reply
        var reply: Replay? = null

        /**
         * 反馈内容
         */
        @SerializedName("ghicstuorstnxyztijkerstnxyzt")
        // content
        var content: String = ""

        /**
         * 是否已读
         */
        @SerializedName("vwxrijkeefgahijd")
        // read
        var read: Boolean = false

        /**
         * 图片列表
         */
        @SerializedName("jklfmnoipqrlijkeYZAUvwxrpqrlwxys")
        // fileUrls
        var fileUrls: List<String>? = null

        /**
         * 创建时间
         */
        @SerializedName("ghicvwxrijkeefgaxyztijkeXYZTmnoiqrsmijkeXYZTijkebcdxxyzt")
        // createTimeText
        var createTimeText: String = ""

        /**
         * 回复状态，0=未回复，1=已回复
         *
         */
        @SerializedName("vwxrijketuvppqrlcdeyWXYSxyztefgaxyztyzauwxys")
        // replyStatus
        var replyStatus: Int = 0

        /**
         * 是否已读
         */
        @SerializedName("jklfijkeijkehijdfghbefgaghicopqkMNOIhijd")
        // feedbackId
        var feedbackId: Long = 0

        class Replay {
            /**
             * 回复内容
             */
            @SerializedName("ghicstuorstnxyztijkerstnxyzt")
            // content
            var content: String = ""
        }
    }

} 