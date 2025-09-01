package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 请求图片生成进度出参
 *
 * 完整 JSON：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/image/progress",
 *   "enPath": "/fsdgesdga/DuRsYKB5J4FBdOHb1+Q1JaAhL0zfNNdR",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "uvwqyzauijkeyzauijkeGHICrstnxyzt": 12, // queueCnt
 *       "wxysxyztefgaxyztijke": 0, // state
 *       "abcwefgamnoixyztXYZTmnoiqrsmijke": 600, // waitTime
 *       "xyztefgawxysopqkMNOIhijd": 23123, // taskId
 *       "mnoiqrsmefgaklmgijkeMNOIrstnjklfstuowxys": [
 *         {
 *           "mnoiqrsmefgaklmgijkeMNOIhijd": "imageId", // imageId
 *           "mnoiqrsmklmgYZAUvwxrpqrl": "imgUrl", // imgUrl
 *           "mnoiwxysFGHBpqrlyzauvwxr": true, // isBlur
 *           "mnoiqrsmklmgXYZTcdeytuvpijke": 1, // imgType
 *           "vwxrijkewxysstuopqrlyzauxyztmnoistuorstn": "resolution" // resolution
 *         }
 *       ], // imageInfos
 *       "tuvpvwxrstuoklmgvwxrijkewxyswxys": 90 // progress
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
class GetImageProgressRespDataModel {

    /**
     * 当前排队人数
     *
     */
    @SerializedName("uvwqyzauijkeyzauijkeGHICrstnxyzt")
    // queueCnt
    val queueCnt: Int? = null

    /**
     * 状态 1 在队列中，2进行中，3 任务完成， 4 超时，5 error
     */
    @SerializedName("wxysxyztefgaxyztijke")
    // state
    val state: Int? = null

    /**
     * 预估时间，单位s
     *
     */
    @SerializedName("abcwefgamnoixyztXYZTmnoiqrsmijke")
    // waitTime
    val waitTime: Int? = null

    /**
     * 任务id，64位有符号整型
     *
     */
    @SerializedName("xyztefgawxysopqkMNOIhijd")
    // taskId
    val taskId: Long? = null

    /**
     * 当state=3， imageInfo不为空
     */
    @SerializedName("mnoiqrsmefgaklmgijkeMNOIrstnjklfstuowxys")
    // imageInfos
    val imageInfos: List<ImageInfo>? = null

    /**
     * 进度， 0- 100
     *
     */
    @SerializedName("tuvpvwxrstuoklmgvwxrijkewxyswxys")
    // progress
    val progress: Int? = null

    class ImageInfo {

        /**
         * 图片ID
         *
         */
        @SerializedName("mnoiqrsmefgaklmgijkeMNOIhijd")
        val imageId: String? = null

        /**
         * 图片地址
         */
        @SerializedName("mnoiqrsmklmgYZAUvwxrpqrl")
        val imgUrl: String? = null

        /**
         * 是否模糊
         *
         */
        @SerializedName("mnoiwxysFGHBpqrlyzauvwxr")
        val isBlur: Boolean? = null

        /**
         *
         */
        @SerializedName("mnoiqrsmklmgXYZTcdeytuvpijke")
        val imgType: Int? = null

        /**
         * 分辨率
         */
        @SerializedName("vwxrijkewxysstuopqrlyzauxyztmnoistuorstn")
        val resolution: String? = null
    }
} 