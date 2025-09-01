package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 安卓获取商品列表出参
 *
 * 完整 JSON：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/payment/getGooglePayActivities",
 *   "enPath": "/fsdgesdga/K_h6wOpg1VIZdKeqZ3Cf2KoEHhlyHoQ0pB1u4g4tSYpFTILddfctgw==",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "efgaghicxyztmnoizabvmnoixyztcdeywxys": [
 *         {
 *           "efgaghicxyztmnoizabvmnoixyztcdeyMNOIhijd": "activityId", // activityId
 *           "stuovwxrmnoiklmgmnoirstnEFGAqrsmstuoyzaurstnxyzt": "originAmount", // originAmount
 *           "ghicpqrlmnoiijkerstnxyzt": "client", // client
 *           "ghicvwxrijkehijdmnoixyztwxys": 15, // credits
 *           "fghbstuorstnyzauwxys": 20, // bonus
 *           "efgaghicxyztyzauefgapqrlEFGAqrsmstuoyzaurstnxyzt": "actualAmount", // actualAmount
 *           "ghicyzauvwxrvwxrijkerstnghiccdey": "currency", // currency
 *           "ghicstuohijdijke": "code", // code
 *           "hijdmnoiwxysghicstuoyzaurstnxyztSTUOjklfjklf": "discountOff", // discountOff
 *           "hijdefgacdeywxys": 7, // days
 *           "xyztmnoixyztpqrlijke": "title", // title
 *           "klmgstuostuohijdwxysMNOIhijd": "goodsId" // goodsId
 *         }
 *       ] // activitys
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
class GetGooglePayActivitiesRespDataModel {

    @SerializedName("efgaghicxyztmnoizabvmnoixyztcdeywxys")
    val activitys: List<ActivityInfo> = emptyList()

    class ActivityInfo {
        @SerializedName("efgaghicxyztmnoizabvmnoixyztcdeyMNOIhijd")
        // activityId
        val activityId: String = ""

        @SerializedName("stuovwxrmnoiklmgmnoirstnEFGAqrsmstuoyzaurstnxyzt")
        // originAmount
        val originAmount: String? = null

        @SerializedName("ghicpqrlmnoiijkerstnxyzt")
        // client
        val client: String? = null

        @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
        // credits
        val credits: Int? = null

        @SerializedName("fghbstuorstnyzauwxys")
        // bonus
        val bonus: Int? = null

        @SerializedName("efgaghicxyztyzauefgapqrlEFGAqrsmstuoyzaurstnxyzt")
        // actualAmount
        val actualAmount: String? = null

        @SerializedName("ghicyzauvwxrvwxrijkerstnghiccdey")
        // currency
        val currency: String? = null

        @SerializedName("ghicstuohijdijke")
        // code
        val code: String? = null

        @SerializedName("hijdmnoiwxysghicstuoyzaurstnxyztSTUOjklfjklf")
        // discountOff
        val discountOff: String? = null

        @SerializedName("hijdefgacdeywxys")
        // days
        val days: Int? = null

        @SerializedName("xyztmnoixyztpqrlijke")
        // title
        val title: String? = null

        @SerializedName("klmgstuostuohijdwxysMNOIhijd")
        // goodsId
        val goodsId: String? = null


    }
}