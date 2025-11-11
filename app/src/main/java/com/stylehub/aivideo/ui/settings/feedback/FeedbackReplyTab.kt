package com.stylehub.aivideo.ui.settings.feedback

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.network.model.out.FeedbackListRespDataModel
import com.stylehub.aivideo.ui.common.CommonEmptyView

/**
 *
 * Create by league at 2025/11/2
 *
 * Write some description here
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackReplyTab(
    loading: Boolean = false,
    feedbackList: List<FeedbackListRespDataModel.FeedbackContent>,
    onLoadData: (isRefresh: Boolean) -> Unit
) {

    val pullToRefreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem?.index == feedbackList.size - 1
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadData(false)
        }
    }

    // 当切换到Tab时加载数据
    LaunchedEffect(Unit) {
        onLoadData(false)
    }

    PullToRefreshBox(
        modifier = Modifier.fillMaxSize(),
        state = pullToRefreshState,
        isRefreshing = loading,
        onRefresh = { onLoadData(true) }
    ) {

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {

            items(feedbackList) {

                Column(modifier = Modifier.padding(15.dp)) {

                    Text(
                        text = it.createTimeText,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.size(5.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color(0xFF393939), RoundedCornerShape(8.dp))
                            .border(1.dp, Color.White, RoundedCornerShape(8.dp))
                            .padding(15.dp)
                    ) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                "me: ",
                                fontSize = 16.sp,
                                color = Color.White
                            )
                            Text(
                                text = it.content,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        }

                        if (it.reply != null) {

                            Spacer(modifier = Modifier.size(5.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "    -reply: ",
                                    fontSize = 16.sp,
                                    color = Color.White.copy(0.8f)
                                )
                                Text(
                                    text = it.reply!!.content,
                                    fontSize = 16.sp,
                                    color = Color.White.copy(0.8f)
                                )
                            }
                        }

                    }

                }
            }

            if (!loading && feedbackList.isEmpty()) {
                item {
                    CommonEmptyView()
                }
            }
        }
    }
}