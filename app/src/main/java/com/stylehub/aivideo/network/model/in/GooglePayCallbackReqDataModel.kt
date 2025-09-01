package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 谷歌支付成功结果回调入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/payment/googlepay",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VK6cFCJnBfV2MaaVtYXYsV8",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "yzauwxysijkevwxrMNOIhijd": "userid", // userId
 *             "tuvpyzauvwxrghiclmnhefgawxysijkeHIJDefgaxyztefga": "purchaseData", // purchaseData
 *             "mnoihijd": "id", // id
 *             "wxysmnoiklmgrstnefgaxyztyzauvwxrijke": "signature" // signature
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "3f7n0pg3" // 固定传参
 * }
 */
class GooglePayCallbackReqDataModel : ReqDataInterface {
    override fun getEncKey(): String = "3f7n0pg3"

    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    var userId: String? = null

    @SerializedName("tuvpyzauvwxrghiclmnhefgawxysijkeHIJDefgaxyztefga")
    var purchaseData: String? = null

    @SerializedName("mnoihijd")
    var id: String? = null

    @SerializedName("wxysmnoiklmgrstnefgaxyztyzauvwxrijke")
    var signature: String? = null
} 