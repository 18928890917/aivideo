package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取用户自己的生成作品列表出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/mycreation",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4Hrala8+u9l2kSyC8x_zAZM",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "ghicstuoyzaurstnxyztMNOIhijd": "countId", // countId
 *             "ghicyzauvwxrvwxrijkerstnxyzt": 1, // current
 *             "wxysijkeefgavwxrghiclmnhGHICstuoyzaurstnxyzt": false, // searchCount
 *             "xyztstuoxyztefgapqrl": 19, // total
 *             "tuvpefgaklmgijkewxys": 5, // pages
 *             "vwxrijkeghicstuovwxrhijdwxys": [
 *                 {
 *                     "tuvpvwxrstuoqrsmtuvpxyzt": "prompt", // prompt
 *                     "mnoiqrsmefgaklmgijkeMNOIhijd": 8745, // imageId
 *                     "efgatuvptuvpvwxrefgamnoiwxysijke": "appraise", // appraise
 *                     "mnoiqrsmklmgYZAUvwxrpqrl": "imgUrl", // imgUrl
 *                     "xyztefgawxysopqkXYZTcdeytuvpijke": 2, // taskType
 *                     "mnoiqrsmklmgXYZTcdeytuvpijke": 1, // imgType
 *                     "fghbpqrlyzauvwxr": false, // blur
 *                     "vwxrijkewxysstuopqrlyzauxyztmnoistuorstn": "resolution" // resolution
 *                 }
 *             ], // records
 *             "stuovwxrhijdijkevwxrwxys": [], // orders
 *             "stuotuvpxyztmnoiqrsmmnoidefzijkeGHICstuoyzaurstnxyztWXYSuvwqpqrl": true, // optimizeCountSql
 *             "wxysmnoidefzijke": 20, // size
 *             "qrsmefgabcdxPQRLmnoiqrsmmnoixyzt": 50 // maxLimit
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     "vwxrefgarstnhijd6782": "iL0UcImc", // 固定传参
 *     "vwxrefgarstnhijd7893": "wAoUGRNU", // 固定传参
 *     "vwxrefgarstnhijd5671": "sOVJY638", // 固定传参
 *     "vwxrefgarstnhijd4560": "NkSwAZA2" // 固定传参
 * }
 */
data class MyCreationRespDataModel(
    @SerializedName("ghicstuoyzaurstnxyztMNOIhijd")
    val countId: String?,
    @SerializedName("ghicyzauvwxrvwxrijkerstnxyzt")
    val current: Int?,
    @SerializedName("wxysijkeefgavwxrghiclmnhGHICstuoyzaurstnxyzt")
    val searchCount: Boolean?,
    @SerializedName("xyztstuoxyztefgapqrl")
    val total: Int?,
    @SerializedName("tuvpefgaklmgijkewxys")
    val pages: Int?,
    @SerializedName("vwxrijkeghicstuovwxrhijdwxys")
    val records: List<MyCreationRecord>?,
    @SerializedName("stuovwxrhijdijkevwxrwxys")
    val orders: List<Any>?,
    @SerializedName("stuotuvpxyztmnoiqrsmmnoidefzijkeGHICstuoyzaurstnxyztWXYSuvwqpqrl")
    val optimizeCountSql: Boolean?,
    @SerializedName("wxysmnoidefzijke")
    val size: Int?,
    @SerializedName("qrsmefgabcdxPQRLmnoiqrsmmnoixyzt")
    val maxLimit: Int?
)

/**
 * records 字段
 */
data class MyCreationRecord(
    @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
    val prompt: String?,
    @SerializedName("mnoiqrsmefgaklmgijkeMNOIhijd")
    val imageId: Long?,
    @SerializedName("efgatuvptuvpvwxrefgamnoiwxysijke")
    val appraise: String?,
    @SerializedName("mnoiqrsmklmgYZAUvwxrpqrl")
    val imgUrl: String?,
    @SerializedName("xyztefgawxysopqkXYZTcdeytuvpijke")
    val taskType: Int?,
    @SerializedName("mnoiqrsmklmgXYZTcdeytuvpijke")
    val imgType: Int?,
    @SerializedName("fghbpqrlyzauvwxr")
    val blur: Boolean?,
    @SerializedName("vwxrijkewxysstuopqrlyzauxyztmnoistuorstn")
    val resolution: String?
) 