package com.stylehub.aivideo.ui.purchase

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.ui.common.CommonEmptyView
import com.stylehub.aivideo.ui.theme.AiSwapTheme

class PurchaseActivity : BaseActivity<PurchaseActivityViewModel, PurchaseActivityData>() {
    override val mViewModel: PurchaseActivityViewModel by viewModels()

    @Composable
    override fun ProvideContent(uiStateData: PurchaseActivityData) {
        AiSwapTheme {
            PurchaseScreen(
                uiStateData = uiStateData,
                onBackClick = { finish() },
                onRefresh = { mViewModel.refreshGoods() },
                onSelectItem = { index -> mViewModel.selectItem(index = index) },
                onPurchase = { mViewModel.handlePurchase() }
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        mViewModel.onActivityResult(requestCode, resultCode, data)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PurchaseScreen(
    uiStateData: PurchaseActivityData,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit = {},
    onSelectItem: (index: Int) -> Unit = {},
    onPurchase: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Purchase Credits",
                        color = Color.White,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
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
                actions = {
                    val context = LocalContext.current
                    IconButton(onClick = {
                        context.startActivity(
                            Intent(
                                context,
                                com.stylehub.aivideo.ui.history.HistoryActivity::class.java
                            )
                        )
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            contentDescription = "History",
                            tint = Color.White
                        )
                    }
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF131313)
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF131313))
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(innerPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item { TopIllustration("${uiStateData.credits}") }
                    item { Spacer(modifier = Modifier.height(24.dp)) }
                    item { BenefitsSection() }
                    item { Spacer(modifier = Modifier.height(24.dp)) }

                    // 显示加载状态
                    if (uiStateData.isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = Color.White
                                )
                            }
                        }
                    }

                    items(uiStateData.purchaseList.activitys.size) { index ->

                        val good = uiStateData.purchaseList.activitys[index]

                        Box {

                            // 渲染商品项
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 20.dp, top = 21.dp, end = 20.dp)
                                    .clip(RoundedCornerShape(32.dp))
                                    .border(
                                        width = 1.dp,
                                        color = Color(0x80FFFFFF),
                                        shape = RoundedCornerShape(32.dp)
                                    )
                                    .background(
                                        brush = Brush.linearGradient(
                                            if (index == uiStateData.selectedItem) {
                                                listOf(Color(0xFF9DA0E6), Color(0xFF9CE6A6))
                                            } else {
                                                listOf(Color.Transparent, Color.Transparent)
                                            }
                                        ),
                                        shape = RoundedCornerShape(32.dp),
                                        alpha = 0.5f
                                    )
                                    .clickable { onSelectItem(index) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Text(
                                        text = "${good.credits ?: 0} Credits",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )

                                    if ((good.bonus ?: 0) > 0) {
                                        Spacer(Modifier.width(10.dp))
                                        Text(
                                            text = "+${good.bonus} Bonus",
                                            fontSize = 12.sp,
                                            style = TextStyle(
                                                brush = Brush.linearGradient(
                                                    listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                                                )
                                            )
                                        )
                                    }

                                    Spacer(Modifier.weight(1f))

                                    if (good.originAmount != null && good.originAmount != good.actualAmount) {
                                        Text(
                                            text = "$${good.originAmount}",
                                            color = if (uiStateData.selectedItem == index) Color.White else Color(
                                                0xFF888989
                                            ),
                                            fontSize = 12.sp,
                                            textDecoration = TextDecoration.LineThrough
                                        )
                                    }

                                    Spacer(Modifier.width(5.dp))

                                    Text(
                                        text = "$${good.actualAmount ?: "-"}",
                                        color = if (uiStateData.selectedItem == index) Color.White else Color(
                                            0xFF888989
                                        ),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )

                                    Spacer(modifier = Modifier.width(30.dp))

                                }
                            }

                            //渲染折扣
                            if (!good.discountOff.isNullOrEmpty() && good.discountOff != "0") {

                                Box(
                                    modifier = Modifier
                                        .padding(top = 5.dp, end = 14.dp)
                                        .align(Alignment.TopEnd)
                                ) {

                                    Image(
                                        painter = painterResource(R.drawable.bg_discount),
                                        contentDescription = "discount",
                                        modifier = Modifier
                                            .width(69.dp)
                                            .height(24.22.dp),
                                    )

                                    Text(
                                        text = "${good.discountOff}% OFF",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiStateData.purchaseList.activitys.isNotEmpty()) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 15.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        PurchaseButton(onPurchase = onPurchase)
                    }
                }

                //无数据展示
                if (!uiStateData.isLoading && uiStateData.purchaseList.activitys.isEmpty()) {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .weight(0.8f),
                        contentAlignment = Alignment.Center
                    ) {
                        CommonEmptyView()
                    }
                }
            }
        }
    }
}

@Composable
private fun TopIllustration(credits: String) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val imageSize = screenWidth / 2

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.mipmap.img_credits),
            contentDescription = "Purchase illustration",
            modifier = Modifier
                .width(imageSize)
                .aspectRatio(1f)
        )
        Box(
            modifier = Modifier
                .size(imageSize * 0.66f)
                .align(Alignment.TopCenter)
                .offset(y = (-20).dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Credits\n$credits",
                color = Color.Black,
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun BenefitsSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        BenefitItem(iconRes = R.drawable.ic_credits, text = "15 Credits")
        BenefitItem(iconRes = R.drawable.ic_priority_queue, text = "Priority queue")
        BenefitItem(iconRes = R.drawable.ic_credits_life, text = "Credits life: 7days")
        BenefitItem(iconRes = R.drawable.ic_multi_creations_create, text = "Multi-creations create")
    }
}

@Composable
private fun BenefitItem(iconRes: Int, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
private fun PurchaseButton(
    onPurchase: () -> Unit,
) {
    Button(
        onClick = onPurchase,
        modifier = Modifier
            .width(148.dp)
            .height(50.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                    ),
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Purchase",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
} 