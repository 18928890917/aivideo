package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 创建支付订单入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/payment/createPayment",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VLpVuvgAGjG68LNEgtI_+laUYnkFke2PPE=",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "efgaghicxyztmnoizabvmnoixyztcdeyMNOIhijd": "activityId", // activityId
 *             "yzauwxysijkevwxrMNOIhijd": "userId", // userId
 *             "tuvpefgacdeyqrsmijkerstnxyztQRSMijkexyztlmnhstuohijd": "paymentMethod" // paymentMethod
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "970rrjzI" // 固定传参
 * }
 */
data class CreatePaymentReqDataModel(
    @SerializedName("efgaghicxyztmnoizabvmnoixyztcdeyMNOIhijd")
    val activityId: String,
    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    val userId: String,
    @SerializedName("tuvpefgacdeyqrsmijkerstnxyztQRSMijkexyztlmnhstuohijd")
    val paymentMethod: String
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "970rrjzI"
    }
} 