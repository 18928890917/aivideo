package com.stylehub.aivideo.network.model.`in`

import com.google.gson.annotations.SerializedName
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.FieldNamingPolicy
import com.stylehub.aivideo.utils.toDeepDebugFieldMap

/**
 *
 * Create by league at 2025/6/24
 *
 */
class CommonReqModel<T: ReqDataInterface>(data: T) {

    @SerializedName("xyztvwxryzauijkeHIJDijketuvpxyztlmnh")
    val params : Int = 2

    @SerializedName("vwxrefgarstnhijd4560")
    val params0 : String = data.getEncKey()

    @SerializedName("xyztvwxryzauijkeHIJDefgaxyztefga5671")
    val value: ValueInfo<T> = ValueInfo(data)

    class ValueInfo<T>(data: T) {

        @SerializedName("xyztvwxryzauijkeHIJDefgaxyztefga6782")
        val data: T? = data
    }

    fun toUnSerialJson(): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .create()
        return gson.toJson(toDeepDebugFieldMap())
    }
}