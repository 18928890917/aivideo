package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 创建支付订单出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/payment/createPayment",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VLpVuvgAGjG68LNEgtI_+laUYnkFke2PPE=",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "tuvpefgacdeyWXYSxyztefgaxyztyzauwxys": "payStatus", // payStatus
 *             "mnoihijd": "id" // id
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     ...
 * }
 */
class CreatePaymentRespDataModel {
    @SerializedName("tuvpefgacdeyWXYSxyztefgaxyztyzauwxys")
    // payStatus
    val payStatus: String? = null

    @SerializedName("mnoihijd")
    // id
    val id: String? = null
} 