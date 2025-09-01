package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 谷歌支付成功结果回调出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/payment/googlepay",
 *     "enPath": "/fsdgesdga/K_h6wOpg1VK6cFCJnBfV2MaaVtYXYsV8",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "mnoihijd": "id", // id
 *             "wxysxyztefgaxyztyzauwxys": "status" // status
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     "vwxrefgarstnhijd6782": "63ygfrHn", // 固定传参
 *     "vwxrefgarstnhijd7893": "MRHiBDgb", // 固定传参
 *     "vwxrefgarstnhijd5671": "cotTg0cZ", // 固定传参
 *     "vwxrefgarstnhijd4560": "F64HrHfh" // 固定传参
 * }
 */
data class GooglePayCallbackRespDataModel(

    @SerializedName("mnoihijd")
    val id: String?,
    @SerializedName("wxysxyztefgaxyztyzauwxys")
    val status: String?
)
