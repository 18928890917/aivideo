package com.stylehub.aivideo.ui.purchase

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.android.billingclient.api.BillingClient.BillingResponseCode
import com.android.billingclient.api.Purchase.PurchaseState
import com.android.billingclient.api.QueryProductDetailsResult
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.CreatePaymentReqDataModel
import com.stylehub.aivideo.network.model.`in`.GooglePayCallbackReqDataModel
import com.stylehub.aivideo.network.model.out.CreatePaymentRespDataModel
import com.stylehub.aivideo.network.model.out.GetGooglePayActivitiesRespDataModel
import com.stylehub.aivideo.network.model.out.GooglePayCallbackRespDataModel
import com.stylehub.aivideo.utils.GooglePaymentUtil
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * Create by league at 2025/7/1
 *
 */
class PurchaseActivityData {

    /**
     * 点数
     */
    var credits by mutableIntStateOf(0)

    /**
     * 充值列表
     */
    var purchaseList by mutableStateOf(GetGooglePayActivitiesRespDataModel())

    /**
     * loading状态
     */
    var isLoading by mutableStateOf(false)

    /**
     * 当前选中的项
     */
    var selectedItem by mutableIntStateOf(0)
}

class PurchaseActivityViewModel(initialValue: PurchaseActivityData = PurchaseActivityData()) :
    BaseViewModel<PurchaseActivityData>(initialValue) {

//    private val LOAD_PAYMENT_DATA_REQUEST_CODE: Int = 909394
//    private lateinit var paymentsClient: PaymentsClient

    private val mutableData = _uiStateData.value

    private var isBillingClientReady = false


    private fun updateCredits() {
        mutableData.credits = LoginManager.getCredit()
    }

    private fun fetchGoods() {
        viewModelScope.launch {
            try {
                mutableData.isLoading = true

                val api = Network().createApi(ApiService::class.java)
                val result = withContext(Dispatchers.IO) {
                    val resp = api.getGooglePayActivities()
                    resp.execute().body()?.data?.value ?: GetGooglePayActivitiesRespDataModel()
                }
                mutableData.purchaseList = result
            } catch (e: Exception) {
                mutableData.purchaseList = GetGooglePayActivitiesRespDataModel()
            } finally {
                mutableData.isLoading = false
            }
        }
    }

    private suspend fun createPayment(actInfo: GetGooglePayActivitiesRespDataModel.ActivityInfo): CreatePaymentRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null

        try {

            val req = CreatePaymentReqDataModel(actInfo.activityId, userId, "GOOGLEPAY")

            val api = Network().createApi(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                val resp = api.createPayment(CommonReqModel(req), userId)
                resp.execute().body()?.data?.value
            }
            return result

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private suspend fun successPayment(createPaymentId: String, purchaseData: String): GooglePayCallbackRespDataModel? {

        val userId = LoginManager.getUserId() ?: return null

        try {

            val req = GooglePayCallbackReqDataModel().apply {
                this.id = createPaymentId
                this.userId = userId
                this.purchaseData = purchaseData
            }

            val api = Network().createApi(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                val resp = api.googlePayCallback(CommonReqModel(req), userId)
                resp.execute().body()?.data?.value
            }
            return result

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 刷新商品列表
     */
    fun refreshGoods() {
        fetchGoods()
    }

    fun selectItem(index: Int) {
        mutableData.selectedItem = index
    }

    /**
     * 处理购买操作
     */
    fun handlePurchase() {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        viewModelScope.launch {

            showLoadingDialog()
            val activityInfo = mutableData.purchaseList.activitys.get(mutableData.selectedItem)
            val paymentInfoResp = createPayment(activityInfo)
            if (paymentInfoResp == null) {
                dismissLoadingDialog()
                ToastUtil.show("Fail")
                return@launch
            }
            //todo 接入谷歌支付
//            isReadyToPay()

            val isInit = GooglePaymentUtil.initBillingClient(mActivity!!) { billingResult,
                                                                            purchases ->

                //购买结果返回
                if (billingResult.responseCode == BillingResponseCode.OK && purchases != null) {
                    for (purchase in purchases) {
                        // Process the purchase as described in the next section.
                        if (purchase.purchaseState == PurchaseState.PURCHASED) {

                            showLoadingDialog("Please Wait")
                            viewModelScope.launch {
                                val successPaymentResult = successPayment(paymentInfoResp.id!!, purchase.originalJson)
                                if (successPaymentResult == null) {
                                    ToastUtil.show("Fail")
                                } else {
                                    if ("success".equals(successPaymentResult.status, true)) {
                                        //消耗此商品
                                        ToastUtil.show("Success")

                                        LoginManager.updateUserAccountSuspend()
                                        updateCredits()
                                    }
                                }
                                dismissLoadingDialog()
                            }
                        }
                    }
                } else if (billingResult.responseCode == BillingResponseCode.USER_CANCELED) {
                    // Handle an error caused by a user canceling the purchase flow.
                    ToastUtil.show("User canceled")
                } else {
                    // Handle any other error codes.
                    ToastUtil.show("Error code: " + billingResult.responseCode)
                }
            }

            if (!isInit) {
                dismissLoadingDialog()
                ToastUtil.show("Connect Google Play Service Fail")
                return@launch
            }

            val queryResult: QueryProductDetailsResult? = GooglePaymentUtil.queryProduct(activityInfo.code ?: "")
            if (queryResult == null || queryResult.productDetailsList.size == 0) {
                dismissLoadingDialog()
                ToastUtil.show("No Product Found")
                return@launch
            }
            val productDetail = queryResult.productDetailsList[0]
            GooglePaymentUtil.launchBillingFlow(mActivity!!, productDetail)
            dismissLoadingDialog()
        }

    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateCredits()
        fetchGoods()

//        paymentsClient = Wallet.getPaymentsClient(
//            mActivity!!,
//            Wallet.WalletOptions.Builder()
//                .setEnvironment(WalletConstants.ENVIRONMENT_TEST) // 上线时改为 PRODUCTION
//                .build()
//        )

//        isReadyToPay()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        GooglePaymentUtil.endConnection()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        LoginManager.updateUserAccount {
            updateCredits()
        }
    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        updateCredits()
    }

//    private fun isReadyToPay() {
//        val isReadyToPayJson = JSONObject()
//            .put("allowedPaymentMethods", JSONArray().put("CARD").put("TOKENIZED_CARD"))
//        val request = IsReadyToPayRequest.fromJson(isReadyToPayJson.toString())
//        paymentsClient.isReadyToPay(request).addOnCompleteListener { task ->
//            if (task.isSuccessful && task.result == true) {
//                // 可以拉起支付
//                loadPaymentData()
//            } else {
//                // 设备不支持 Google Pay
//            }
//        }
//    }
//
//    private fun loadPaymentData() {
//        val paymentDataRequestJson = JSONObject()
//            .put("allowedPaymentMethods", JSONArray().put("CARD").put("TOKENIZED_CARD"))
//            .put(
//                "transactionInfo", JSONObject()
//                    .put("totalPrice", "1.99")
//                    .put("totalPriceStatus", "FINAL")
//                    .put("currencyCode", "USD")
//            )
//            .put(
//                "merchantInfo", JSONObject()
//                    .put("merchantName", "Example Merchant")
//            )
//        val request = fromJson(paymentDataRequestJson.toString())
//        AutoResolveHelper.resolveTask(
//            paymentsClient.loadPaymentData(request),
//            mActivity!!,
//            LOAD_PAYMENT_DATA_REQUEST_CODE
//        )
//    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (requestCode == LOAD_PAYMENT_DATA_REQUEST_CODE) {
//            when (resultCode) {
//                Activity.RESULT_OK -> {
//                    val paymentData = PaymentData.getFromIntent(data!!)
//                    // 处理支付成功
//                }
//
//                Activity.RESULT_CANCELED -> {
//                    // 用户取消
//                }
//
//                AutoResolveHelper.RESULT_ERROR -> {
//                    val status = AutoResolveHelper.getStatusFromIntent(data)
//                    // 处理错误
//                }
//            }
//        }
//    }
}
