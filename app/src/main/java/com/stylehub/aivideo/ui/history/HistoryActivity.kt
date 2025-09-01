@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.stylehub.aivideo.ui.history

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.ui.theme.AiSwapTheme

class HistoryActivity: BaseActivity<HistoryActivityViewModel, HistoryActivityData>() {
    override val mViewModel: HistoryActivityViewModel by viewModels()

    @Composable
    override fun ProvideContent(uiStateData: HistoryActivityData) {
        AiSwapTheme {
            HistoryScreen(
                onBackClick = { finish() },
                uiStateData = uiStateData,
                onLoadMore = { mViewModel.loadMoreData() },
                onRefresh = { mViewModel.refreshData() }
            )
        }
    }
}

data class HistoryItem(val title: String, val time: String, val amount: Int)

@Composable
fun HistoryScreen(
    onBackClick: () -> Unit,
    uiStateData: HistoryActivityData,
    onLoadMore: () -> Unit = {},
    onRefresh: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "History",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF131313)
                )
            )
        },
        containerColor = Color(0xFF131313)
    ) { innerPadding ->
        val listState = rememberLazyListState()
        val shouldLoadMore = remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem?.index == uiStateData.creditsHistoryList.size - 1
            }
        }

        LaunchedEffect(shouldLoadMore.value) {
            if (shouldLoadMore.value && !uiStateData.isLoading && uiStateData.hasMoreData) {
                onLoadMore()
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF131313))
                .padding(innerPadding),
        ) {
            if (uiStateData.creditsHistoryList.isEmpty() && !uiStateData.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No credits history found",
                            color = Color(0xFF888888),
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(uiStateData.creditsHistoryList) { item ->
                    HistoryListItem(
                        HistoryItem(
                            title = item.title,
                            time = item.time,
                            amount = item.amount
                        )
                    )
                }
            }
            
            if (uiStateData.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun GradientText(text: String, fontSize: TextUnit, fontWeight: FontWeight, modifier: Modifier = Modifier) {
    val gradient = Brush.horizontalGradient(listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6)))
    BasicText(
        text = text,
        modifier = modifier,
        style = TextStyle(
            brush = gradient,
            fontSize = fontSize,
            fontWeight = fontWeight
        )
    )
}

@Composable
fun HistoryListItem(item: HistoryItem) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 12.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(
                    text = item.time,
                    color = Color(0xFF888888),
                    fontSize = 14.sp
                )
            }
            if (item.amount > 0) {
                GradientText(
                    text = "+${"%,d".format(item.amount)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            } else {
                Text(
                    text = item.amount.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0x22FFFFFF))
        )
    }
} 