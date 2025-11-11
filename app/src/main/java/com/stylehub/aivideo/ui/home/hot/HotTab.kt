package com.stylehub.aivideo.ui.home.hot

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.home.HomeActivity

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun HotTab(
    innerPadding: PaddingValues,
    homeActivity: HomeActivity,
    onTabRequest: (tab: String) -> Unit
) {

    val viewModel = homeActivity.mViewModel
    val hotTabData = viewModel.uiStateData.value.hotTabData
    val pullToRefreshState = rememberPullToRefreshState()

    // 当切换到DanceTab时加载数据
    LaunchedEffect(Unit) {
        viewModel.getUserCommonInfo()
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize().padding(innerPadding),
        state = pullToRefreshState,
        isRefreshing = hotTabData.isLoading,
        onRefresh = { viewModel.getUserCommonInfo(true) }
    ) {
        Box(Modifier.fillMaxSize()) {

            //无数据
            if (!hotTabData.isLoading && hotTabData.configList.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CommonEmptyView()
                }
            }

            val map = hotTabData.configList.groupBy { it.tag }

            if (map.size > 1) {
                HotTabPanelB(
                    map,
                    {viewModel.switchTab(it)},
                    onTabRequest
                )
            } else {
                HotTabPanelA(
                    hotTabData.configList,
                    {viewModel.switchTab(it)},
                    onTabRequest
                )
            }
        }
    }
}
