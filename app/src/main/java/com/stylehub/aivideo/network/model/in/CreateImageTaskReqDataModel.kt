package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 通用图生任务入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/image/create-task",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4HwSk6p_bz8R8m+1w7CqCny",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "tuvpvwxrstuoqrsmtuvpxyzt": "prompt", // prompt
 *             "xyztefgawxysopqkXYZTcdeytuvpijke": "taskType", // taskType
 *             "wxysvwxrghicMNOIqrsmklmg5671": "srcImg1", // srcImg1
 *             "wxysmnoidefzijke": "size", // size
 *             "ijkebcdxxyzt": "", // ext
 *             "xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke": "templateName", // templateName
 *             "wxysvwxrghicMNOIqrsmklmg6782": "srcImg2" // srcImg2
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "tL1UdG5C" // 固定传参
 * }
 */
data class CreateImageTaskReqDataModel(
    @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
    var prompt: String? = null,
    @SerializedName("xyztefgawxysopqkXYZTcdeytuvpijke")
    var taskType: String? = null,
    @SerializedName("wxysvwxrghicMNOIqrsmklmg5671")
    var srcImg1: String? = null,
    @SerializedName("wxysmnoidefzijke")
    var size: String? = null,
    @SerializedName("ijkebcdxxyzt")
    var ext: String? = null,
    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeRSTNefgaqrsmijke")
    var templateName: String? = null,
    @SerializedName("wxysvwxrghicMNOIqrsmklmg6782")
    var srcImg2: String? = null,
    @SerializedName("rstnijkeijkehijdstuotuvpxyzt")
    var needOpt: Boolean? = null
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "tL1UdG5C"
    }
} 