package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 我的任务列表（含进行中和已完成）出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/my-tasks",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4GowM1dU3cCJqh1XhMX5YSX",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "ghicyzauvwxrvwxrijkerstnxyzt": 1, // current
 *             "lmnhefgawxysRSTNijkebcdxxyzt": true, // hasNext
 *             "xyztstuoxyztefgapqrl": 2, // total
 *             "vwxrijkeghicstuovwxrhijdwxys": [
 *                 {
 *                     "wxysxyztefgaxyztijke": "finished", // state
 *                     "mnoiqrsmklmgPQRLmnoiwxysxyzt": [
 *                         {
 *                             "mnoiqrsmklmgYZAUvwxrpqrl": "https://xxx.png", // imgUrl
 *                             "mnoiqrsmklmgXYZTcdeytuvpijke": 1, // imgType
 *                             "fghbpqrlyzauvwxr": false, // blur
 *                             "vwxrijkewxysstuopqrlyzauxyztmnoistuorstn": "383*511" // resolution
 *                         }
 *                     ], // imgList
 *                     "xyztefgawxysopqkXYZTcdeytuvpijke": 4, // taskType
 *                     "xyztefgawxysopqkMNOIhijd": 12994 // taskId
 *                 }
 *             ], // records
 *             "wxysmnoidefzijke": 20 // size
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "success", // msg
 *     "vwxrefgarstnhijd6782": "HOTN4o2S", // 固定传参
 *     "vwxrefgarstnhijd7893": "VI1xysXI", // 固定传参
 *     "vwxrefgarstnhijd5671": "WL5ct2LQ", // 固定传参
 *     "vwxrefgarstnhijd4560": "gcVs3xfm" // 固定传参
 * }
 */
data class MyTasksRespDataModel(
    @SerializedName("ghicyzauvwxrvwxrijkerstnxyzt")
    val current: Int?,
    @SerializedName("lmnhefgawxysRSTNijkebcdxxyzt")
    val hasNext: Boolean?,
    @SerializedName("xyztstuoxyztefgapqrl")
    val total: Int?,
    @SerializedName("vwxrijkeghicstuovwxrhijdwxys")
    val records: List<MyTaskRecord>?,
    @SerializedName("wxysmnoidefzijke")
    val size: Int?
)

/**
 * records 字段
 */
data class MyTaskRecord(
    /**
     * 任务状态
     * pending=进行中，
     * finished=已完成，
     * failed=失败
     *
     */
    @SerializedName("wxysxyztefgaxyztijke")
    val state: String?,
    /**
     * 图片或视频列表
     */
    @SerializedName("mnoiqrsmklmgPQRLmnoiwxysxyzt")
    val imgList: List<MyTaskImage>?,
    /**
     * 任务类型
     * 2=图片换脸，3=换衣，5=视频换脸，7=黏土风格，8=跳舞视频，11=高级换脸
     */
    @SerializedName("xyztefgawxysopqkXYZTcdeytuvpijke")
    val taskType: Int?,
    /**
     * 任务id，64位有符号整型
     *
     */
    @SerializedName("xyztefgawxysopqkMNOIhijd")
    val taskId: Long?
)

/**
 * imgList 字段
 */
data class MyTaskImage(
    /**
     * 图片或视频地址
     */
    @SerializedName("mnoiqrsmklmgYZAUvwxrpqrl")
    val imgUrl: String?,
    /**
     * 图片类型，1=图片，2=视频
     */
    @SerializedName("mnoiqrsmklmgXYZTcdeytuvpijke")
    val imgType: Int?,
    /**
     * 是否加了模糊
     */
    @SerializedName("fghbpqrlyzauvwxr")
    val blur: Boolean?,
    /**
     * 分辨率
     * e.g:"383*511"
     */
    @SerializedName("vwxrijkewxysstuopqrlyzauxyztmnoistuorstn")
    val resolution: String?
) 