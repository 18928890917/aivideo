package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName

/**
 * 获取支付订单入参
 *
 * 完整 JSON：
 * {
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 2, // 固定传参
 *     "rawPath": "/v1/payment/getPaymentDetailList",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VL5JVd+wj40z+yisaqC9Uiwu8t26t1oMLeks7we_aXu0g==",
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga6782": {
 *             "yzauwxysijkevwxrMNOIhijd": "userId", // userId
 *             "jklfmnoipqrlxyztijkevwxrWXYSxyztefgaxyztyzauwxys": "filterStatus", // filterStatus
 *             "tuvpefgacdeyqrsmijkerstnxyztQRSMijkexyztlmnhstuohijd": "paymentMethod" // paymentMethod
 *         }
 *     },
 *     "vwxrefgarstnhijd4560": "G6aGtnt6" // 固定传参
 * }
 */
data class GetPaymentDetailListReqDataModel(
    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    val userId: String,
    @SerializedName("jklfmnoipqrlxyztijkevwxrWXYSxyztefgaxyztyzauwxys")
    val filterStatus: String?,
    @SerializedName("tuvpefgacdeyqrsmijkerstnxyztQRSMijkexyztlmnhstuohijd")
    val paymentMethod: String?
) : ReqDataInterface {
    override fun getEncKey(): String {
        return "G6aGtnt6"
    }
} 