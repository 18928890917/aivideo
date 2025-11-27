package com.stylehub.aivideo.ui.mine

import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.common.CommonLoading
import com.stylehub.aivideo.ui.dialog.PreviewDialog
import com.stylehub.aivideo.utils.AppRouterManager
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

class MineActivity : BaseActivity<MineActivityViewModel, MineActivityUiData>() {
    override val mViewModel: MineActivityViewModel by viewModels()

    @Composable
    override fun ProvideContent(uiStateData: MineActivityUiData) {
        MineScreen(
            uiStateData = uiStateData,
            onBack = { finish() },
            onSettings = {
                AppRouterManager.enterSettingsActivity()
            },
            onPurchase = {
                AppRouterManager.enterPurchaseActivity()
            },
            onSwitchTab = {
                mViewModel.switchTab(it)
                if (it == 0) {
                    mViewModel.loadLatestSwappedTask()
                } else {
                    mViewModel.loadLatestDanceTask()
                }
            },
            onPreview = {
                mViewModel.showPreviewDialog(it)
            },
            onDownload = {
                mViewModel.download(it)
            },
            onLoadMore = {
                if (it == 0) {
                    mViewModel.loadSwappedTask()
                } else {
                    mViewModel.loadOtherTask()
                }
            },
            onLoadLatest = {
                if (it == 0) {
                    mViewModel.loadLatestSwappedTask()
                } else {
                    mViewModel.loadLatestDanceTask()
                }
            }
        )

        PreviewDialog(
            show = uiStateData.showPreviewDialog,
            onDismiss = { mViewModel.dismissPreviewDialog() },
            onDownloadClick = { mViewModel.download() },
            model = mViewModel.getCurrentPreviewUrl(),
            mediaType = mViewModel.getCurrentPreviewType(),
            title = "${mViewModel.currentPreviewModel?.taskId ?: ""}"
        )
    }
}

@OptIn(FlowPreview::class)
@Composable
fun LoadMoreWhenApproachingEnd(
    listState: LazyListState,
    threshold: Int = 5,
    debounceMs: Long = 200L,
    onLoadMore: () -> Unit,
) {
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: -1
            val total = listState.layoutInfo.totalItemsCount
            lastVisible to total
        }
            .distinctUntilChanged()
            .filter { (last, total) -> total > 0 && last >= total - threshold }
            .debounce(debounceMs)
            .collect { onLoadMore() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(
    uiStateData: MineActivityUiData,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onPurchase: () -> Unit,
    onSwitchTab: (index: Int) -> Unit,
    onPreview: (TaskListData) -> Unit,
    onDownload: (TaskListData) -> Unit,
    onLoadMore: (tabIndex: Int) -> Unit,
    onLoadLatest: (tabIndex: Int) -> Unit
) {
    val swappedListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }
    val danceListState = rememberSaveable(saver = LazyListState.Saver) { LazyListState() }

    val swapPullToRefreshState = remember {
        object : PullToRefreshState {

            private val anim = Animatable(0f, Float. VectorConverter)

            override val distanceFraction: Float
                get() = anim. value

            override suspend fun animateToHidden() {
                anim. animateTo(0f)
            }

            override suspend fun animateToThreshold() {
//                anim. animateTo(1f, spring(dampingRatio = Spring. DampingRatioHighBouncy))
                anim. animateTo(0f)
            }

            override suspend fun snapTo(targetValue: Float) {
                anim. snapTo(targetValue)
            }
        }
    }
    val dancePullToRefreshState = remember {
        object : PullToRefreshState {

            private val anim = Animatable(0f, Float. VectorConverter)

            override val distanceFraction: Float
                get() = anim. value

            override suspend fun animateToHidden() {
                anim.animateTo(0f)
            }

            override suspend fun animateToThreshold() {
//                anim. animateTo(1f, spring(dampingRatio = Spring. DampingRatioHighBouncy))
                anim.animateTo(0f)
            }

            override suspend fun snapTo(targetValue: Float) {
                anim.snapTo(targetValue)
            }
        }
    }

    LoadMoreWhenApproachingEnd(swappedListState) { onLoadMore(0) }
    LoadMoreWhenApproachingEnd(danceListState) { onLoadMore(1) }

    Scaffold { pd ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF181A20))
                .padding(horizontal = 24.dp)
                .padding(pd)
        ) {
            // 顶部栏
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = onSettings) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }

            // 头像区
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                AvatarBox(avatarUrl = uiStateData.avatarUrl)
            }
            Text(
                text = uiStateData.userName,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 8.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Credits区
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp)
                    .clickable { onPurchase() }
                    .border(width = Dp(1f), shape = RoundedCornerShape(100), color = Color.White)
                    .padding(start = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${uiStateData.credits} credits",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "Purchase",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                )

                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Default.ChevronRight,
                        contentDescription = "Purchase",
                        tint = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 内容区（任务列表）
            Column(
                modifier = Modifier
                    .border(0.5.dp, Color(0x20FFFFFF), RoundedCornerShape(18.dp))
                    .shadow(1.dp, RoundedCornerShape(5.dp))
                    .fillMaxSize()
            ) {
                SegmentedTab(
                    options = uiStateData.Tabs,
                    selectedIndex = uiStateData.currentSelectIndex,
                    onSelected = onSwitchTab,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 8.dp)
                )

                Text(
                    text = "Content is valid for 24 hours.Please save it before it expires",
                    fontSize = 12.sp,
                    color = Color(0xFF5A5B5B),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = "Process ID",
                    fontSize = 12.sp,
                    color = Color(0xFF5A5B5B),
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(start = 16.dp, top = 10.dp)
                )

                if (uiStateData.currentSelectIndex == 0) {

                    PullToRefreshBox(
                        isRefreshing = uiStateData.swappedTaskLoadLatestLoading,
                        onRefresh = { onLoadLatest(0) },
                        state = swapPullToRefreshState,
                        indicator = {
                            if (!uiStateData.swappedTaskLoadLatestLoading) {
                                Indicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    isRefreshing = false,
                                    state = swapPullToRefreshState
                                )
                            }
                        }
                    ) {
                        LazyColumn(state = swappedListState) {

                            if (uiStateData.swappedTaskLoadLatestLoading) {
                                item {
                                    CommonLoading()
                                }
                            }

                            items(uiStateData.swappedTaskList.size) { idx ->
                                TaskListItem(
                                    task = uiStateData.swappedTaskList[idx],
                                    onDownload = onDownload,
                                    onPreview = onPreview
                                )
                                // 在每个项目后添加一个 Divider，除了最后一个项目
                                if (idx < uiStateData.swappedTaskList.size - 1) {
                                    HorizontalDivider(
                                        thickness = 0.5.dp, // 分割线厚度
                                        color = Color(0xFF5A5B5B) // 分割线颜色
                                    )
                                }
                            }
                            if (uiStateData.swappedTaskLoading) {
                                item {
                                    CommonLoading()
                                }
                            } else {
                                if (!uiStateData.swappedTaskLoadLatestLoading && uiStateData.swappedTaskList.isEmpty()) {
                                    item {
                                        CommonEmptyView()
                                    }
                                }
                            }
                        }
                    }

                } else {

                    PullToRefreshBox(
                        isRefreshing = uiStateData.danceTaskLoadLatestLoading,
                        onRefresh = { onLoadLatest(1) },
                        state = dancePullToRefreshState,
                        indicator = {
                            if (!uiStateData.danceTaskLoadLatestLoading) {
                                Indicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    isRefreshing = false,
                                    state = dancePullToRefreshState
                                )
                            }
                        }
                    ) {
                        LazyColumn(state = danceListState) {

                            if (uiStateData.danceTaskLoadLatestLoading) {
                                item {
                                    CommonLoading()
                                }
                            }

                            items(uiStateData.danceTaskList.size) { idx ->
                                TaskListItem(
                                    task = uiStateData.danceTaskList[idx],
                                    onDownload = onDownload,
                                    onPreview = onPreview
                                )
                                // 在每个项目后添加一个 Divider，除了最后一个项目
                                if (idx < uiStateData.danceTaskList.size - 1) {
                                    HorizontalDivider(
                                        thickness = 0.5.dp, // 分割线厚度
                                        color = Color(0xFF5A5B5B) // 分割线颜色
                                    )
                                }
                            }
                            if (uiStateData.danceTaskLoading) {
                                item {
                                    CommonLoading()
                                }
                            } else {
                                if (!uiStateData.danceTaskLoadLatestLoading && uiStateData.danceTaskList.isEmpty()) {
                                    item {
                                        CommonEmptyView()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun AvatarBox(avatarUrl: String?) {
    Box(
        modifier = Modifier
            .size(120.dp),
        contentAlignment = Alignment.Center
    ) {
        // 背景图
        Image(
            painter = painterResource(id = R.drawable.bg_avatar),
            contentDescription = "Avatar Background",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        // 头像
        if (!avatarUrl.isNullOrBlank()) {
            AsyncImage(
                model = avatarUrl,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape),
                placeholder = painterResource(id = R.drawable.ic_profile_default),
                error = painterResource(id = R.drawable.ic_profile_default)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_profile_default),
                contentDescription = "Default Avatar",
                modifier = Modifier
                    .size(70.dp)
                    .clip(CircleShape)
            )
        }
    }
}

@Composable
fun SegmentedTab(
    options: List<String>,
    selectedIndex: Int,
    onSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(40.dp),
        verticalAlignment = Alignment.Top
    ) {
        options.forEachIndexed { index, text ->
            val selected = index == selectedIndex

            if (index == 1) {
                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(17.dp)
                        .background(Color(0xFF2B2B2B))
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .clickable { onSelected(index) },
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = text,
                    color = if (!selected) Color(0xFF5A5B5B) else Color.White,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun TaskListItem(
    task: TaskListData,
    onDownload: (TaskListData) -> Unit,
    onPreview: (TaskListData) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task.taskId?.toString() ?: "--",
            color = Color.White,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f)
        )

        if (task.taskState == TaskState.FINISHED) {
            TextButton(onClick = { onDownload(task) }) {
                Text(
                    "Download",
                    fontSize = 15.sp,
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF9D9FE7),
                                Color(0xFF627564)
                            )
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.width(3.5.dp))
            Spacer(
                modifier = Modifier
                    .width(1.dp)
                    .height(17.dp)
                    .background(Color(0xFF2B2B2B))
            )
            Spacer(modifier = Modifier.width(3.5.dp))
            TextButton(onClick = { onPreview(task) }) {
                Text("Preview", fontSize = 15.sp, color = Color(0xFFD6DAFA))
            }
        } else if (task.taskState == TaskState.PENDING) {
            Text("Pending - ${task.progress ?: 0}%", fontSize = 15.sp, color = Color(0xFFD6DAFA))
        } else {
            Text("Fail", fontSize = 15.sp, color = Color(0xFFD6DAFA))
        }

    }
}
