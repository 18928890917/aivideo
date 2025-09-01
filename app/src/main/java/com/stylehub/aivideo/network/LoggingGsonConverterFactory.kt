package com.stylehub.aivideo.network

import android.util.Log
import com.google.gson.Gson
import com.stylehub.aivideo.BuildConfig
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.reflect.Type

class LoggingGsonConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {
    private val delegateFactory = GsonConverterFactory.create(gson)

    companion object {
        fun create(): LoggingGsonConverterFactory = create(Gson())
        fun create(gson: Gson): LoggingGsonConverterFactory = LoggingGsonConverterFactory(gson)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegate = delegateFactory.responseBodyConverter(type, annotations, retrofit)
        return Converter<ResponseBody, Any> { body ->
            val result = delegate?.convert(body)
            if (BuildConfig.DEBUG) {

                if (result is CommonRespModel<*>) {
                    Log.d("ApiDebug", "[GSON CONVERTED] ${result.toUnSerialJson()}")
                } else {
                    Log.d("ApiDebug", "[GSON CONVERTED] null")
                }
            }

            result
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        // 获取委托工厂的转换器
        val converter = delegateFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)

        // 返回包装后的转换器
        @Suppress("UNCHECKED_CAST")
        return RequestBodyConverterWrapper(converter as Converter<Any, RequestBody>)
    }

    // 定义一个泛型包装类
    private class RequestBodyConverterWrapper<T>(private val delegate: Converter<T, RequestBody>) :
        Converter<T, RequestBody> {
        override fun convert(value: T): RequestBody? {
            if (value is CommonReqModel<*>) {
                Log.d("ApiDebug", "[Params NOT CONVERTED]\n ${value.toUnSerialJson()}")
            }
            return delegate.convert(value)
        }
    }


}