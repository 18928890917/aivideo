package com.stylehub.aivideo.ui.settings.feedback

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.FeedbackReadReqDataModel
import com.stylehub.aivideo.network.model.`in`.FeedbackSubmitReqDataModel
import com.stylehub.aivideo.network.model.out.FeedbackListRespDataModel
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

class FeedbackActivityUiData {

    var selectTab by mutableIntStateOf(0)
    var isLoading by mutableStateOf(false)
    val pageSize = 10
    var currentPage: Int by mutableIntStateOf(1)
    var total: Int by mutableIntStateOf(1)
    // 声明
    val feedbackList = mutableStateListOf<FeedbackListRespDataModel.FeedbackContent>()

//    var versionName by mutableStateOf("")
//    var contactUrl by mutableStateOf("")
//    var isLogin by mutableStateOf(false)
//    var showDevDialog by mutableStateOf(false)
}

class FeedbackActivityViewModel : BaseViewModel<FeedbackActivityUiData>(FeedbackActivityUiData()) {

    private val mutableData = _uiStateData.value

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateInfo()

        LoginManager.updateUserAccount {
            updateInfo()
        }
    }

    private fun updateInfo() {

//        mutableData.isLogin = LoginManager.isGoogleLogin()
//        mutableData.versionName = "Version ${AppUtil.getVersionName()}"
//        mutableData.contactUrl = LoginManager.getEmail()?:"xxx.com"
    }

    fun login() {
        showLoginDialog()
    }

    fun logout() {
        LoginManager.logout()
        updateInfo()
    }

    override fun onLoginSuccess() {
        updateInfo()
    }

    fun switchToReplyTabAndRefresh() {
        mutableData.selectTab = 1
        loadData(true)
    }

    fun loadData(isRefresh: Boolean = false) {

        if (mutableData.isLoading)
            return

        if (isRefresh) {
            mutableData.currentPage = 1
            mutableData.currentPage = 1
            mutableData.feedbackList.clear()
        }

        if (mutableData.feedbackList.size >= mutableData.total)
            return

        viewModelScope.launch {

            if (mutableData.isLoading)
                return@launch
            mutableData.isLoading = true

            val feedbackData = getFeedbackList(mutableData.currentPage, mutableData.pageSize)
            if (feedbackData != null) {

                if (feedbackData.records != null) {
                    mutableData.feedbackList.addAll(feedbackData.records!!)
                }
                mutableData.total = feedbackData.total
                mutableData.currentPage++
            }
            mutableData.isLoading = false
        }
    }

    fun submit(content: String) {

        if (content.trim().isEmpty()) {
            ToastUtil.show("please type content description")
            return
        }

        showLoadingDialog("please wait...")
        viewModelScope.launch {

            val result = feedbackSubmit(content)
            if (result) {
                ToastUtil.show("submit fail")
            } else {
                switchToReplyTabAndRefresh()
            }
            dismissLoadingDialog()
        }
    }


    /**
     * 获取反馈列表
     */
    private suspend fun getFeedbackList(
        currentSize: Int,
        pageSize: Int = mutableData.pageSize,
        replayState: Int? = null
    ): FeedbackListRespDataModel? {

        try {

            val api = Network().createApi(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                val resp = api.getFeedbackList(
                    currentSize, pageSize, replayState
                )
                resp.execute().body()?.data?.value
            }
            return result

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 提交反馈
     */
    private suspend fun feedbackSubmit(
        content: String,
    ): Boolean {

        try {

            val req = FeedbackSubmitReqDataModel(content)

            val api = Network().createApi(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                val resp = api.feedbackSubmit(
                    CommonReqModel(req)
                )
                resp.execute().body()?.data?.value
            }
            return result ?: false

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    /**
     * 已读反馈
     */
    private suspend fun feedbackRead(
        feedbackId: String,
    ): Boolean {

        try {

            val req = FeedbackReadReqDataModel(arrayListOf(feedbackId))

            val api = Network().createApi(ApiService::class.java)
            val result = withContext(Dispatchers.IO) {
                val resp = api.feedbackRead(
                    CommonReqModel(req)
                )
                resp.execute().body()?.data?.value
            }
            return result ?: false

        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }
}