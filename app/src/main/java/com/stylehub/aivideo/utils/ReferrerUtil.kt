package com.stylehub.aivideo.utils

import android.content.Context
import com.android.installreferrer.api.InstallReferrerClient
import com.android.installreferrer.api.InstallReferrerStateListener
import com.android.installreferrer.api.ReferrerDetails
import com.facebook.applinks.AppLinkData
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.consts.AppConst
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.ReportReferrerReqDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 *
 * Create by league at 2025/11/12
 *
 * Write some description here
 */
object ReferrerUtil {

    @OptIn(DelicateCoroutinesApi::class)
    fun submitGoogleReferrer(context: Context) {

        val referrerClient = InstallReferrerClient.newBuilder(context).build()
        referrerClient.startConnection(object : InstallReferrerStateListener {
            override fun onInstallReferrerSetupFinished(responseCode: Int) {
                when (responseCode) {
                    InstallReferrerClient.InstallReferrerResponse.OK -> {
                        // Connection established
                        try {
                            val response: ReferrerDetails = referrerClient.installReferrer
                            val referrerUrl = response.installReferrer
//                            val clickTimestamp = response.referrerClickTimestampSeconds
//                            val installTimestamp = response.installBeginTimestampSeconds
                            // 你可以在这里处理 referrer 信息，比如上报或存储
                            SharedPreferenceUtil.put(PrefKey.GOOGLE_REFERRER_URL, referrerUrl)

                            GlobalScope.launch {

                                val isUploadReferer = submitReferrer(referrerUrl)
                                val isUpload: Boolean =
                                    SharedPreferenceUtil.get(
                                        PrefKey.IS_UPLOAD_GOOGLE_REFERER,
                                        false
                                    ) == false
                                if (!isUpload) {
                                    //记录结果
                                    SharedPreferenceUtil.put(
                                        PrefKey.IS_UPLOAD_GOOGLE_REFERER,
                                        isUploadReferer
                                    )
                                }
                            }

                        } catch (e: Exception) {
                            e.printStackTrace()
                        } finally {
                            referrerClient.endConnection()
                        }
                    }

                    InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED -> {
                        // API 不支持
                    }

                    InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE -> {
                        // 服务不可用
                    }
                }
            }

            override fun onInstallReferrerServiceDisconnected() {
                // 连接断开，可以重试
            }
        })
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun submitFacebookReferrer(context: Context) {

        AppLinkData.fetchDeferredAppLinkData(context) { data ->
            val params = mapOf("url" to data?.targetUri)
//            //打点
//            LogUtils.logFBReferrerMessage(Gson().toJson(params))
            //归因Url
            val url = data?.targetUri?.toString()

            if (url != null) {
                //保存Url到本地
                SharedPreferenceUtil.put(PrefKey.FACEBOOK_REFERRER_URL, url)

                //上报归因
                GlobalScope.launch {
                    val res = submitReferrer(url, AppConst.ReferrerType.FACEBOOK)
                    //记录是否已上传过facebook归因
                    if (SharedPreferenceUtil.get(
                            PrefKey.IS_UPLOAD_FACEBOOK_REFERER,
                            false
                        ) == false
                    ) {
                        SharedPreferenceUtil.put(PrefKey.IS_UPLOAD_FACEBOOK_REFERER, res)
                    }
                }
            }

        }

    }


    suspend fun submitReferrer(
        referrerUrl: String,
        rt: AppConst.ReferrerType = AppConst.ReferrerType.GOOGLE
    ): Boolean {

        return suspendCoroutine {

            val api = Network().createApi(ApiService::class.java)

            val request = ReportReferrerReqDataModel(
                referrerUrl,
                AppUtil.getDeviceId()
            )

            api.reportReferrer(CommonReqModel(request), rt.type).enqueue(object :
                Callback<CommonRespModel<Unit>> {
                override fun onResponse(
                    call: Call<CommonRespModel<Unit>>,
                    response: Response<CommonRespModel<Unit>>
                ) {
                    if (response.isSuccessful && response.body()?.code == 0) {
                        it.resume(true)
                        return
                    }
                    it.resume(false)
                }

                override fun onFailure(
                    call: Call<CommonRespModel<Unit>>,
                    t: Throwable
                ) {
                    it.resume(false)
                }

            })
        }
    }
}