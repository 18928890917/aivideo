package com.stylehub.aivideo.network.model.out

import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.stylehub.aivideo.utils.toDeepDebugFieldMap

/**
 *
 * Create by league at 2025/6/24
 *
 * 通用返回模型
 */
class CommonRespModel<T> {

    @SerializedName("ghicstuohijdijke")
    val code: Int = 0 // code

    @SerializedName("xyztvwxryzauijkeHIJDijketuvpxyztlmnh")
    val params: Int = 1 // 固定传参

    @SerializedName("hijdefgaxyztefga")
    val data: DataInfo<T>? = null

    @SerializedName("qrsmwxysklmg")
    val msg: String? = null // msg

    @SerializedName("vwxrefgarstnhijd4560")
    val params0: String? = null // 固定传参

    @SerializedName("vwxrefgarstnhijd5671")
    val params1: String? = null // 固定传参

    @SerializedName("vwxrefgarstnhijd6782")
    val params2: String? = null // 固定传参

    @SerializedName("vwxrefgarstnhijd7893")
    val params3: String? = null // 固定传参


    class DataInfo<T> {
        @SerializedName("xyztvwxryzauijkeHIJDefgaxyztefga5671")
        val value: T? = null
    }

    fun toUnSerialJson(): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        return gson.toJson(toDeepDebugFieldMap())
    }
}