package com.stylehub.aivideo.ui.settings.feedback

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.base.BaseActivity
import kotlinx.coroutines.launch

class FeedbackActivity : BaseActivity<FeedbackActivityViewModel, FeedbackActivityUiData>() {
    override val mViewModel: FeedbackActivityViewModel by viewModels()

    @Composable
    override fun ProvideContent(uiStateData: FeedbackActivityUiData) {

        FeedbackScreen(
            uiStateData = uiStateData,
            onBack = { finish() },
            onSubmitClick = {mViewModel.submit(it)},
            onLoadFeedback = {mViewModel.loadData(it)},
            switchPageIndex = uiStateData.selectTab
        )
    }
}

@Composable
fun FeedbackScreen(
    uiStateData: FeedbackActivityUiData,
    onBack: () -> Unit,
    onSubmitClick: (content: String) -> Unit,
    onLoadFeedback: (isRefresh: Boolean) -> Unit,
    switchPageIndex: Int = 0
) {
    val tabs = arrayOf("Feedback", "Reply")
    val pagerState = rememberPagerState { tabs.size }
    val scope = rememberCoroutineScope()

    LaunchedEffect(switchPageIndex) {
        if (pagerState.currentPage != switchPageIndex &&
            switchPageIndex in tabs.indices
        ) {
            scope.launch {
                pagerState.animateScrollToPage(switchPageIndex)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF111212))
            .padding(WindowInsets.statusBars.asPaddingValues())
            .padding(bottom = 54.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 顶部栏
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
            }
            Text(
                text = "Feedback",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        TabRow(
            pagerState.currentPage,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    modifier = Modifier.background(Color(0xFF131313)),
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 16.sp,
                        )
                    }
                )
            }

        }

        // --- 内容区域：HorizontalPager ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->

            if (page == 0) {
                FeedbackSubmitTab(
                    onSubmitClick = onSubmitClick
                )
            } else {
                FeedbackReplyTab(
                    uiStateData.isLoading,
                    uiStateData.feedbackList,
                    onLoadFeedback
                )
            }
        }
    }
}

