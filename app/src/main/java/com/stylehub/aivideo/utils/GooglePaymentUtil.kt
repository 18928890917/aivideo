package com.stylehub.aivideo.utils

import android.app.Activity
import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.PendingPurchasesParams
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryProductDetailsResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


object GooglePaymentUtil {
    private var billingClient: BillingClient? = null

    suspend fun initBillingClient(context: Context, listener: PurchasesUpdatedListener): Boolean {

        return suspendCoroutine {

            if (billingClient == null) {

                billingClient = BillingClient.newBuilder(context)
                    .setListener(listener)
                    .enableAutoServiceReconnection()
                    .enablePendingPurchases(
                        PendingPurchasesParams.newBuilder()
                            .enablePrepaidPlans()
                            .enableOneTimeProducts()
                            .build()
                    )
                    .build()
            }
            billingClient?.startConnection(object : BillingClientStateListener {
                override fun onBillingSetupFinished(billingResult: BillingResult) {
                    if (billingResult.responseCode == BillingResponseCode.OK) {
                        it.resume(true)
                    } else {
                        it.resume(retryBillingServiceConnection())
                    }
                }

                override fun onBillingServiceDisconnected() {
                    retryBillingServiceConnection()
                }
            })
        }
    }

    private fun retryBillingServiceConnection(): Boolean {
        val maxTries = 3
        var tries = 1
        var isConnectionEstablished = false
        do {
            try {
                billingClient?.startConnection(object : BillingClientStateListener {
                    override fun onBillingServiceDisconnected() {
                    }

                    override fun onBillingSetupFinished(billingResult: BillingResult) {
                        if (billingResult.responseCode == BillingResponseCode.OK) {
                            isConnectionEstablished = true
                        }
                    }
                })
            } catch (e: Exception) {
                e.message?.let {
                    //do nothing
                }
            } finally {
                tries++
            }
        } while (tries <= maxTries && !isConnectionEstablished)

        return isConnectionEstablished
    }


    suspend fun queryProduct(
        productId: String
    ): QueryProductDetailsResult? {

        return suspendCoroutine {
            val productList: MutableList<QueryProductDetailsParams.Product> = mutableListOf()

            productList.add(
                QueryProductDetailsParams.Product.newBuilder()
                    .setProductId(productId)
                    .setProductType(BillingClient.ProductType.INAPP)
                    .build()
            )

            val params = QueryProductDetailsParams.newBuilder()
                .setProductList(productList)
                .build()

            billingClient!!.queryProductDetailsAsync(
                params
            ) { billingResult,
                result ->

                if (billingResult.responseCode == BillingResponseCode.OK) {
                    it.resume(result)
                } else {
                    it.resume(null)
                }

            }
        }

    }

    fun launchBillingFlow(activity: Activity, productDetails: ProductDetails) {

        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(productDetails)
                        .build()
                )
            )
            .build()


        // 启动购买流程
        billingClient!!.launchBillingFlow(activity, billingFlowParams)
    }

    fun endConnection() {
        billingClient?.endConnection()
    }
}