package com.stylehub.aivideo.network

import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.stylehub.aivideo.AiSwapApplication
import com.stylehub.aivideo.BuildConfig
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.network.consts.FieldEncryptDict
import com.stylehub.aivideo.network.consts.UrlEncryptDict
import com.stylehub.aivideo.utils.EncryptUtil
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.NetworkUtil
import com.stylehub.aivideo.utils.SharedPreferenceUtil
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit

class EncryptAndLogInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var urlStr = original.url.toString()

        // 路径加密
        UrlEncryptDict.urlMap.forEach { (origin, encrypted) ->
            urlStr = urlStr.replace(origin, encrypted)
        }
        // 参数名加密
        FieldEncryptDict.dict.forEach { (original, encrypted) ->
            urlStr = urlStr.replace("$original=", "$encrypted=")
        }
        val newRequest = original.newBuilder().url(urlStr).build()

        // 日志打印（仅debug包）
        if (BuildConfig.DEBUG) {
            val sb = StringBuilder()

            sb.append("\n[API CALL] Original URL:").append(original.url)
            sb.append("\n[API CALL] Headers: ").append(original.headers)

            sb.append("\n")
            sb.append("\n[API CALL] Encrypt URL:").append(newRequest.url)
            sb.append("\n[API CALL] Headers: ").append(newRequest.headers)
            // 打印参数体
            newRequest.body?.let { body ->
                try {
                    val buffer = okio.Buffer()
                    body.writeTo(buffer)
                    val charset = body.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                    val bodyStr = buffer.readString(charset)
                    sb.append("\n[API CALL] Params: \n").append(formatJson(bodyStr))
                } catch (e: Exception) {
                    sb.append("\n[API CALL] Params: <unreadable>")
                }
            }

            Log.d("ApiDebug", sb.toString())
        }

        val response = chain.proceed(newRequest)

        // --- 新增：解密响应体 ---
        val rawBody = response.body
        val key = "bwrgnajwelnuycpq"
        if (rawBody != null) {
            val rawString = rawBody.string()
            val decrypted = EncryptUtil.aesDecrypt(rawString, key, isBase64 = true)
            val newBody = decrypted.toResponseBody(rawBody.contentType())

            val newResp = response.newBuilder().body(newBody).build()

            // 日志打印回调体（仅debug包）
            if (BuildConfig.DEBUG) {
                try {
                    val respBody = newResp.peekBody(Long.MAX_VALUE).string()
                    Log.d("ApiDebug", "[API RESP] URL: ${newRequest.url}\n[API RESP] Body: ${formatJson(respBody)}")
                } catch (_: Exception) {}
            }

            return newResp
        }

        // 日志打印回调体（仅debug包）
        if (BuildConfig.DEBUG) {
            try {
                val respBody = response.peekBody(Long.MAX_VALUE).string()
                Log.d("ApiDebug", "[API RESP] URL: ${newRequest.url}\n[API RESP] Body: ${formatJson(respBody)}")
            } catch (_: Exception) {}
        }
        return response
    }

    private fun formatJson(json: String): String {
        return try {
            val obj = JsonParser.parseString(json)
            GsonBuilder().setPrettyPrinting().create().toJson(obj)
        } catch (_: Exception) {
            json
        }
    }
}

class TokenInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        var urlStr = original.url.toString()
        if (urlStr == "/v1/payment/getGooglePayActivities" ||
            urlStr == UrlEncryptDict.urlMap.get("/v1/payment/getGooglePayActivities")) {
            return chain.proceed(original)
        }
        val token = LoginManager.getUserToken()
        return if (!token.isNullOrBlank()) {
            val newRequest = original.newBuilder()
                .addHeader("user_token", token)
                .build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(original)
        }
    }
}

class EncryptProxyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url
        val method = original.method
        val headers = original.headers
        val key = "bwrgnajwelnuycpq"
        val appValue = "stylehub"
        val proxyUrl = originalUrl.newBuilder()
            .encodedQuery(null)
            .encodedPath("/ira/pm")
            .build()

        // 1. 处理url和参数
        val urlPath = originalUrl.newBuilder().encodedQuery(null).build().toString()
        val urlParams = mutableMapOf<String, String>()
        originalUrl.queryParameterNames.forEach { name ->
            urlParams[name] = originalUrl.queryParameter(name) ?: ""
        }
        val urlParamsJson = if (urlParams.isNotEmpty()) Gson().toJson(urlParams) else null

        // 2. 处理header
        val headerMap = mutableMapOf<String, String>()
        for (i in 0 until headers.size) {
            headerMap[headers.name(i)] = headers.value(i)
        }
        val headerJson = if (headerMap.isNotEmpty()) Gson().toJson(headerMap) else null

        // 3. 处理body
        var bodyStr: String? = null
        var formStr: String? = null
        var filePart: MultipartBody.Part? = null
        if (method == "POST" || method == "PUT") {
            val body = original.body
            if (body != null) {
                val buffer = okio.Buffer()
                body.writeTo(buffer)
                val contentType = body.contentType()?.toString() ?: ""
                val raw = buffer.readUtf8()
                if (contentType.contains("application/json")) {
                    bodyStr = raw
                } else if (contentType.contains("application/x-www-form-urlencoded")) {
                    val formMap = raw.split("&").mapNotNull {
                        val parts = it.split("=")
                        if (parts.size == 2) parts[0] to parts[1] else null
                    }.toMap()
                    formStr = Gson().toJson(formMap)
                }
                // 这里可扩展文件上传逻辑
            }
        }

        // 4. 构造form-data
        val formBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
        formBuilder.addFormDataPart("a1", appValue)
        formBuilder.addFormDataPart("u1", EncryptUtil.aes(urlPath, key))
        formBuilder.addFormDataPart("m1", method)
        if (headerJson != null) formBuilder.addFormDataPart("h1", EncryptUtil.aes(headerJson, key))
        if (urlParamsJson != null) formBuilder.addFormDataPart("p1", EncryptUtil.aes(urlParamsJson, key))
        if (bodyStr != null) formBuilder.addFormDataPart("b1", EncryptUtil.aes(bodyStr, key))
        if (formStr != null) formBuilder.addFormDataPart("f1", EncryptUtil.aes(formStr, key))
        // 文件上传可扩展: formBuilder.addFormDataPart("srcVideo", fileName, fileBody)

        val newRequest = original.newBuilder()
            .url(proxyUrl)
            .post(formBuilder.build())
            .build()


        // 日志打印（仅debug包）
        if (BuildConfig.DEBUG) {
            val sb = StringBuilder()

            sb.append("\n[API CALL] Original URL:").append(original.url)
            sb.append("\n[API CALL] Headers: ").append(original.headers)
            // 打印参数体
            original.body?.let { body ->
                try {
                    val buffer = okio.Buffer()
                    body.writeTo(buffer)
                    val charset = body.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                    val bodyStr = buffer.readString(charset)
                    sb.append("\n[API CALL] Params: ").append(formatJson(bodyStr))
                } catch (e: Exception) {
                    sb.append("\n[API CALL] Params: <unreadable>")
                }
            }

            sb.append("\n")
            sb.append("\n[API CALL] Encrypt URL:").append(newRequest.url)
            sb.append("\n[API CALL] Headers: ").append(newRequest.headers)
            // 打印参数体
            newRequest.body?.let { body ->
                try {
                    val buffer = okio.Buffer()
                    body.writeTo(buffer)
                    val charset = body.contentType()?.charset(Charsets.UTF_8) ?: Charsets.UTF_8
                    val bodyStr = buffer.readString(charset)
                    sb.append("\n[API CALL] Params: \n").append(formatJson(bodyStr))
                } catch (e: Exception) {
                    sb.append("\n[API CALL] Params: <unreadable>")
                }
            }

            Log.d("ApiDebug", sb.toString())
        }

        val response = chain.proceed(newRequest)

        // 日志打印回调体（仅debug包）
        if (BuildConfig.DEBUG) {
            try {
                val respBody = response.peekBody(Long.MAX_VALUE).string()
                Log.d("ApiDebug", "[API RESP] URL: ${newRequest.url}\n[API RESP] Body: ${formatJson(respBody)}")
            } catch (_: Exception) {}
        }

        return response
    }

    private fun formatJson(json: String): String {
        return try {
            val obj = JsonParser.parseString(json)
            GsonBuilder().setPrettyPrinting().create().toJson(obj)
        } catch (_: Exception) {
            json
        }
    }
}

/**
 *
 * Create by league at 2025/6/19
 *
 * 使用示例：
 * val api = Network.createApi(ApiService::class.java)
 *
 */
class Network {

//    private val BASE_URL = "https://testapi.fancytool.org"

    private val BASE_URL = "https://api.fancytool.org"

//    private val BASE_URL = "https://api.aiplays.net"

//    private val BASE_URL = "https://devapi.power-ai.xyz"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(TokenInterceptor())
        .addInterceptor(EncryptAndLogInterceptor())
        .addInterceptor(EncryptProxyInterceptor())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
//        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(LoggingGsonConverterFactory.create())
        .build()

    fun <T> createApi(service: Class<T>): T = retrofit.create(service).apply {

        if (!NetworkUtil.hasNetwork()) {
            val act = AiSwapApplication.getInstance().getTopActivity()
            if(act is BaseActivity<*, *>) {
                (act.mViewModel as BaseViewModel<*>).showNetworkOfflineDialog()
            }
        }
    }
}