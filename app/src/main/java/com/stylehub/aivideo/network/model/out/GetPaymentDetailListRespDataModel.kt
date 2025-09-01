package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取支付订单出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/payment/getPaymentDetailList",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VL5JVd+wj40z+yisaqC9Uiwu8t26t1oMLeks7we_aXu0g==",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *             {
 *                 "mnoihijd": "id", // id
 *                 "wxysxyztefgaxyztyzauwxys": "status" // status
 *             }
 *         ]
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     ...
 * }
 */
class GetPaymentDetailListRespDataModel {
    @SerializedName("mnoihijd")
    // id
    val id: String? = null

    @SerializedName("wxysxyztefgaxyztyzauwxys")
    // status
    val status: String? = null
} 