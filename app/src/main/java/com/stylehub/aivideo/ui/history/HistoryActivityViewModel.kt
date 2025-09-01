package com.stylehub.aivideo.ui.history

import androidx.lifecycle.LifecycleOwner
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.model.out.CreditsPageRespDataModel
import com.stylehub.aivideo.utils.LoginManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Create by league at 2025/7/1
 *
 */

data class HistoryActivityData(
    var creditsHistoryList: List<CreditsHistoryItem> = emptyList(),
    var isLoading: Boolean = false,
    var currentPage: Int = 1,
    var hasMoreData: Boolean = true
)

data class CreditsHistoryItem(
    val title: String,
    val time: String,
    val amount: Int,
    val businessType: String,
    val subBusinessType: String?
)

class HistoryActivityViewModel(initialValue: HistoryActivityData = HistoryActivityData()) : BaseViewModel<HistoryActivityData>(
    initialValue
) {

    private val apiService = Network().createApi(ApiService::class.java)
    private val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        fetchCreditsHistory()
    }

    fun fetchCreditsHistory(page: Int = 1, isRefresh: Boolean = false) {
        val userId = LoginManager.getUserId() ?: return
        
        if (isRefresh) {
            _uiStateData.value = _uiStateData.value.copy(
                currentPage = 1,
                hasMoreData = true
            )
        }
        
        _uiStateData.value = _uiStateData.value.copy(isLoading = true)

        apiService.getCreditsPage(
            userId = userId,
            page = page,
            size = 20
        ).enqueue(object : Callback<com.stylehub.aivideo.network.model.out.CommonRespModel<CreditsPageRespDataModel>> {
            override fun onResponse(
                call: Call<com.stylehub.aivideo.network.model.out.CommonRespModel<CreditsPageRespDataModel>>,
                response: Response<com.stylehub.aivideo.network.model.out.CommonRespModel<CreditsPageRespDataModel>>
            ) {
                _uiStateData.value = _uiStateData.value.copy(isLoading = false)
                
                if (response.isSuccessful && response.body()?.data?.value != null) {
                    val creditsData = response.body()!!.data!!.value!!
                    val records = creditsData.records ?: emptyList()
                    
                    val historyItems = records.map { record ->
                        val credits = record.credits ?: 0
                        val amount = when (record.type) {
                            1 -> credits // Increase
                            2 -> -credits // Decrease
                            else -> credits
                        }
                        
                        CreditsHistoryItem(
                            title = getBusinessTypeTitle(record.businessType, record.subBusinessType),
                            time = formatTime(record.createTime),
                            amount = amount,
                            businessType = record.businessType ?: "",
                            subBusinessType = record.subBusinessType
                        )
                    }
                    
                    val currentList = _uiStateData.value.creditsHistoryList
                    val newList = if (isRefresh || page == 1) {
                        historyItems
                    } else {
                        currentList + historyItems
                    }
                    
                    _uiStateData.value = _uiStateData.value.copy(
                        creditsHistoryList = newList,
                        currentPage = creditsData.current ?: 1,
                        hasMoreData = (creditsData.current ?: 1) < (creditsData.pages ?: 1)
                    )
                }
            }

            override fun onFailure(
                call: Call<com.stylehub.aivideo.network.model.out.CommonRespModel<CreditsPageRespDataModel>>,
                t: Throwable
            ) {
                _uiStateData.value = _uiStateData.value.copy(isLoading = false)
                // Handle error - could show toast or error dialog
            }
        })
    }

    fun loadMoreData() {
        if (!_uiStateData.value.isLoading && _uiStateData.value.hasMoreData) {
            fetchCreditsHistory(_uiStateData.value.currentPage + 1)
        }
    }

    fun refreshData() {
        fetchCreditsHistory(isRefresh = true)
    }

    private fun getBusinessTypeTitle(businessType: String?, subBusinessType: String?): String {
        return when (businessType) {
            "recharge" -> "Recharge"
            "register" -> "Sign up"
            "admin" -> "Admin Operation"
            "task" -> {
                when (subBusinessType) {
                    "1" -> "Text to Image"
                    "2" -> "Photo Face Swap"
                    "3" -> "Change Clothes"
                    "5" -> "Video Face Swap"
                    "7" -> "Clay Style"
                    "8" -> "Dance"
                    else -> "Task"
                }
            }
            else -> "Unknown"
        }
    }

    private fun formatTime(timestamp: Long?): String {
        if (timestamp == null) return ""
        return dateFormat.format(Date(timestamp))
    }
}