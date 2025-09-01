package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 *
 * Create by league at 2025/6/25
 *
 * 分类列表返回数据模型
 *
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/category-list",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4H3n85v2RexpoWITjJqY0IB",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": [
 *             {
 *                 "ghicefgaxyztijkeklmgstuovwxrcdeyRSTNefgaqrsmijke": "categoryName", // categoryName
 *                 "xyztijkeqrsmtuvppqrlefgaxyztijkeXYZTcdeytuvpijke": "templateType", // templateType
 *                 "ghicefgaxyztijkeklmgstuovwxrcdeyMNOIhijd": "categoryId" // categoryId
 *             }
 *         ]
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     "WARNING": "*****rawPath、enPath、 WARNING作为说明用，不能写到代码里，rawPath是原始path, enPath是混淆后的********",
 *     "vwxrefgarstnhijd6782": "90GNH13i", // 固定传参
 *     "vwxrefgarstnhijd7893": "6j3HYEPi", // 固定传参
 *     "vwxrefgarstnhijd5671": "iSCOkMPz", // 固定传参
 *     "vwxrefgarstnhijd4560": "aUgTG1sx" // 固定传参
 * }
 */
class CategoryListRespDataModel {

    @SerializedName("ghicefgaxyztijkeklmgstuovwxrcdeyRSTNefgaqrsmijke")
    val categoryName: String? = null

    @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkeXYZTcdeytuvpijke")
    val templateType: String? = null

    @SerializedName("ghicefgaxyztijkeklmgstuovwxrcdeyMNOIhijd")
    val categoryId: Int? = null
}