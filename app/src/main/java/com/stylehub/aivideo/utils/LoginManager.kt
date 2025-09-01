package com.stylehub.aivideo.utils

import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.gson.Gson
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.FastLoginReqDataModel
import com.stylehub.aivideo.network.model.`in`.GoogleLoginReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.LoginResp
import com.stylehub.aivideo.network.model.out.UserCommonInfoRespDataModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * LoginManager
 *
 * 用于一键快速登录，自动构造参数并调用 fastLogin 接口。
 * 支持回调登录结果，并自动缓存 token。
 *
 * 使用示例：
 * LoginManager.fastLogin(context, utm = "utm_source", onSuccess = { resp ->
 *     // 登录成功处理
 * }, onError = { code, msg ->
 *     // 登录失败处理
 * })
 */
object LoginManager {
    private var guestLoginResp: LoginResp? = null
    private var googleLoginResp: LoginResp? = null

    var googleSignInHelper: GoogleSignInHelper? = null

    /**
     * 快速登录
     * @param context Context
     * @param utm 归因参数（如有）
     * @param referer 归因参数（如有）
     * @param sign 设备签名（如有）
     * @param onSuccess 登录成功回调
     * @param onError 登录失败回调（code, message）
     */
    suspend fun fastLogin(
        onSuccess: (LoginResp) -> Unit = {},
        onError: (Int, String) -> Unit = {_,_->}
    ) {
        return suspendCoroutine {

            val deviceId = AppUtil.getDeviceId()
            val referer = SharedPreferenceUtil.get(PrefKey.REFERRER_URL, "")!!
            val sign = EncryptUtil.md5(deviceId, upperCase = true)

            val reqData = FastLoginReqDataModel(
                referer = referer,
                sign = sign,
                deviceId = deviceId
            )
            // 如需手动封装为通用请求体，可用如下方式：
            val body = CommonReqModel(reqData)

            val api = Network().createApi(ApiService::class.java)
            api.fastLogin(body = body).enqueue(object : Callback<CommonRespModel<LoginResp>> {
                override fun onResponse(
                    call: Call<CommonRespModel<LoginResp>>,
                    response: Response<CommonRespModel<LoginResp>>
                ) {
                    val resp = response.body()
                    val data = resp?.data?.value
                    if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {
                        // 缓存用户信息到内存和本地
                        guestLoginResp = data
                        SharedPreferenceUtil.put(PrefKey.GUEST_LOGIN_RESP, Gson().toJson(data))
                        onSuccess(data)
                    } else {
                        onError(resp?.code ?: -1, resp?.msg ?: "login fail")
                    }
                    it.resume(Unit)
                }
                override fun onFailure(
                    call: Call<CommonRespModel<LoginResp>>,
                    t: Throwable
                ) {
                    onError(-1, t.message ?: "network error")
                    it.resume(Unit)
                }
            })
        }

    }

    fun updateUserAccount(onUpdated: (Boolean) -> Unit = {}) {

        if (googleLoginResp == null) {
            onUpdated(false)
            return
        }
        val api = Network().createApi(ApiService::class.java)
        api.getUserAccount(
            userId = getUserId()!!
        ).enqueue(object : Callback<CommonRespModel<LoginResp>> {
            override fun onResponse(
                call: Call<CommonRespModel<LoginResp>>,
                response: Response<CommonRespModel<LoginResp>>
            ) {
                val resp = response.body()
                val data = resp?.data?.value
                if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {

                    val token = getCurrentUser()?.userToken
                    data.userToken = token
                    if (googleLoginResp != null) {
                        googleLoginResp = data
                        SharedPreferenceUtil.put(PrefKey.GOOGLE_LOGIN_RESP, Gson().toJson(data))
                    }
                    onUpdated(false)

                } else {
                    onUpdated(false)
                }
            }
            override fun onFailure(
                call: Call<CommonRespModel<LoginResp>>,
                t: Throwable
            ) {
                // 可选：处理异常
                onUpdated(false)
            }
        })
    }

    suspend fun updateUserAccountSuspend(): Boolean {

        return suspendCoroutine {
            updateUserAccount {
                result->
                it.resume(result)
            }
        }
    }

    fun googleLogin(

        onSuccess: (LoginResp) -> Unit = {},
        onError: (Int, String) -> Unit = {_,_->}
    ) {

        googleSignInHelper?.run {

            onLoginResult = {
                    googleSignInAccount: GoogleIdTokenCredential? ->

                if (googleSignInAccount != null) {

                    val req = GoogleLoginReqDataModel().apply {
                        referer = SharedPreferenceUtil.get(PrefKey.REFERRER_URL, "")!!
                        deviceId = AppUtil.getDeviceId()
                        userName = googleSignInAccount.displayName
                        avatar = googleSignInAccount.profilePictureUri?.toString()
                        code = googleSignInAccount.idToken
                    }

                    val api = Network().createApi(ApiService::class.java)
                    api.googleLogin(CommonReqModel(req)).enqueue(object : Callback<CommonRespModel<LoginResp>> {
                        override fun onResponse(
                            call: Call<CommonRespModel<LoginResp>>,
                            response: Response<CommonRespModel<LoginResp>>
                        ) {
                            val resp = response.body()
                            val data = resp?.data?.value
                            if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {
                                // 缓存用户信息到内存和本地
                                googleLoginResp = data
                                SharedPreferenceUtil.put(PrefKey.GOOGLE_LOGIN_RESP, Gson().toJson(data))
                                onSuccess(data)
                            } else {
                                onError(resp?.code ?: -1, resp?.msg ?: "login fail")
                            }
                        }

                        override fun onFailure(
                            call: Call<CommonRespModel<LoginResp>>,
                            t: Throwable
                        ) {
                            onError(-1, t.message ?: "google login fail")
                        }

                    })
                } else {
                    onError(-1, "Connect fail")
                }
            }

            signIn()
        }

    }

    /**
     * 获取用户通用信息
     */
    suspend fun getUserCommonInfo() : UserCommonInfoRespDataModel? {
        val userId = getUserId() ?: return null

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)
            api.getUserCommonInfo(
                userId = userId
            ).enqueue(object : Callback<CommonRespModel<UserCommonInfoRespDataModel>> {
                override fun onResponse(
                    call: Call<CommonRespModel<UserCommonInfoRespDataModel>>,
                    response: Response<CommonRespModel<UserCommonInfoRespDataModel>>
                ) {
                    val resp = response.body()
                    val data = resp?.data?.value
                    if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {
                        it.resume(data)
                        return
                    }
                    it.resume(null)
                }
                override fun onFailure(
                    call: Call<CommonRespModel<UserCommonInfoRespDataModel>>,
                    t: Throwable
                ) {
                    // 可选：处理异常
                    it.resume(null)
                }
            })
        }
    }

    /**
     * 启动时调用，从SharedPreference加载用户信息到内存
     */
    fun initFromPreference() {
        val guestJson = SharedPreferenceUtil.get<String>(PrefKey.GUEST_LOGIN_RESP, null)
        if (!guestJson.isNullOrEmpty()) {
            guestLoginResp = try {
                Gson().fromJson(guestJson, LoginResp::class.java)
            } catch (_: Exception) {
                null
            }
        }

        val googleJson = SharedPreferenceUtil.get<String>(PrefKey.GOOGLE_LOGIN_RESP, null)
        if (!googleJson.isNullOrEmpty()) {
            googleLoginResp = try {
                Gson().fromJson(googleJson, LoginResp::class.java)
            } catch (_: Exception) {
                null
            }
        }
    }

    private fun getCurrentUser(): LoginResp? {
        return googleLoginResp?: guestLoginResp
    }

    fun getUser(): LoginResp? = getCurrentUser()
    fun getUserId(): String? = getCurrentUser()?.userId
    fun getUserToken(): String? = getCurrentUser()?.userToken
    fun getUserName(): String? = getCurrentUser()?.userName
    fun getAvatar(): String? = getCurrentUser()?.avatar
    fun getEmail(): String? = getCurrentUser()?.email
    fun getCredit(): Int = getCurrentUser()?.credit?:0
    fun getVip(): Boolean? = getCurrentUser()?.vip
    fun getCountryCode(): String? = getCurrentUser()?.countryCode
    fun getUsign(): String? = getCurrentUser()?.usign
    fun getTgName(): String? = getCurrentUser()?.tgName
    fun getTgId(): String? = getCurrentUser()?.tgId
    fun getType(): String? = getCurrentUser()?.type
    fun getTags(): List<String>? = getCurrentUser()?.tags
    fun getStatus(): Int? = getCurrentUser()?.status
    fun getFreeTimes(): Int? = getCurrentUser()?.freeTimes
    fun getFreeBlurTimes(): Int? = getCurrentUser()?.freeBlurTimes
    fun getSubScribeValidTime(): Long? = getCurrentUser()?.subScribeValidTime

    fun isGuestLogin(): Boolean {
        val isLoggedIn = guestLoginResp != null
        return isLoggedIn
    }

    fun isGoogleLogin(): Boolean {
        val isLoggedIn = googleLoginResp != null
        return isLoggedIn
    }

    fun logout() {
        googleSignInHelper?.run {
            this.signOut()
        }
        googleLoginResp = null
        SharedPreferenceUtil.remove(PrefKey.GOOGLE_LOGIN_RESP)
        // 清除UserViewModel
    }
}