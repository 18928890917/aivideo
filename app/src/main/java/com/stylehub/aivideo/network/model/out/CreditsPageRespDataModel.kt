package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 积分记录 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/user/credits-page",
 *   "enPath": "/fsdgesdga/ZOosuuxZLKTHWdt8JIdM0q3wQ4d977X_",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "ghicyzauvwxrvwxrijkerstnxyzt": 1, // current
 *       "xyztstuoxyztefgapqrl": 100, // total
 *       "tuvpefgaklmgijkewxys": 1, // pages
 *       "vwxrijkeghicstuovwxrhijdwxys": [
 *         {
 *           "wxysyzaufghbFGHByzauwxysmnoirstnijkewxyswxysXYZTcdeytuvpijke": "5", // subBusinessType
 *           "fghbyzauwxysmnoirstnijkewxyswxysMNOIhijd": "123123", // businessId
 *           "fghbyzauwxysmnoirstnijkewxyswxysXYZTcdeytuvpijke": "task", // businessType
 *           "ghicvwxrijkehijdmnoixyztwxys": 9, // credits
 *           "ghicvwxrijkeefgaxyztijkeXYZTmnoiqrsmijke": 1737512685906, // createTime
 *           "xyztcdeytuvpijke": 2 // type
 *         }
 *       ], // records
 *       "wxysmnoidefzijke": 10 // size
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class CreditsPageRespDataModel(

    /**
     * 当前页
     */
    @SerializedName("ghicyzauvwxrvwxrijkerstnxyzt")
    val current: Int?,

    /**
     * 总条数
     */
    @SerializedName("xyztstuoxyztefgapqrl")
    val total: Int?,

    /**
     * 总页数
     */
    @SerializedName("tuvpefgaklmgijkewxys")
    val pages: Int?,

    /**
     * 积分记录
     */
    @SerializedName("vwxrijkeghicstuovwxrhijdwxys")
    val records: List<CreditRecord>?,

    /**
     * 每页大小
     */
    @SerializedName("wxysmnoidefzijke")
    val size: Int?
) {
    data class CreditRecord(

        /**
         * ○1=文生图
         * ○2=换脸
         * ○3=自定义换衣
         * ○5=视频换脸
         * ○7=黏土风格
         * ○8=图片生成视频pose
         *
         */
        @SerializedName("wxysyzaufghbFGHByzauwxysmnoirstnijkewxyswxysXYZTcdeytuvpijke")
        val subBusinessType: String?,

        /**
         * 业务id
         */
        @SerializedName("fghbyzauwxysmnoirstnijkewxyswxysMNOIhijd")
        val businessId: String?,

        /**
         * 任务类型：task=任务，recharge=充值，admin=管理员操作，register=注册
         */
        @SerializedName("fghbyzauwxysmnoirstnijkewxyswxysXYZTcdeytuvpijke")
        val businessType: String?,

        /**
         *  点数数量
         */
        @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
        val credits: Int?,

        /**
         * 时间戳
         */
        @SerializedName("ghicvwxrijkeefgaxyztijkeXYZTmnoiqrsmijke")
        val createTime: Long?,

        /**
         * 积分类型，1=增加，2=扣除
         */
        @SerializedName("xyztcdeytuvpijke")
        val type: Int?
    )
} 