package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取用户的付费成功记录 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/user/payments",
 *   "enPath": "/fsdgesdga/ZOosuuxZLKRfUeIuvXkhaMgXVhqu2O28",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *       {
 *         "ghicyzauvwxrvwxrijkerstnghiccdey": "currency", // currency
 *         "efgaqrsmstuoyzaurstnxyzt": 2.5, // amount
 *         "tuvpefgacdeyXYZTmnoiqrsmijke": 1734579800155, // payTime
 *         "tuvpefgacdeyMNOIhijd": "payId", // payId
 *         "tuvpefgacdeyQRSMijkexyztlmnhstuohijd": "payMethod" // payMethod
 *       }
 *     ]
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class UserPaymentsRespDataModel(
    @SerializedName("ghicyzauvwxrvwxrijkerstnghiccdey")
    val currency: String?,
    @SerializedName("efgaqrsmstuoyzaurstnxyzt")
    val amount: Double?,
    @SerializedName("tuvpefgacdeyXYZTmnoiqrsmijke")
    val payTime: Long?,
    @SerializedName("tuvpefgacdeyMNOIhijd")
    val payId: String?,
    @SerializedName("tuvpefgacdeyQRSMijkexyztlmnhstuohijd")
    val payMethod: String?
) 