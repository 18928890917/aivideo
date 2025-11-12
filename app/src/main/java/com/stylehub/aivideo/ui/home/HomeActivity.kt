package com.stylehub.aivideo.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.facebook.appevents.AppEventsLogger
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.ui.home.dance.ClothesSwapTab
import com.stylehub.aivideo.ui.home.hot.HotTab
import com.stylehub.aivideo.ui.home.swap.SwapTab
import com.stylehub.aivideo.ui.theme.AiSwapTheme
import com.stylehub.aivideo.utils.AppRouterManager
import com.stylehub.aivideo.utils.ReferrerUtil

class HomeActivity : BaseActivity<HomeActivityViewModel, HomeActivityData>() {
    override val mViewModel: HomeActivityViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    @Composable
    override fun ProvideContent(uiStateData: HomeActivityData) {

        AiSwapTheme {
            HomeScreen(this, uiStateData)
        }

//        val hotDataList = mutableListOf<UserConfigDataModel>()
//
//        val commonData = UserConfigDataModel()
//        commonData.title = "Common Video"
//        commonData.description = "Upload photos to start making"
//        commonData.imgUrl = "https://cdn.fancytool.org/cdn/f/20250805/jv1754372568551.jpeg"
//        commonData.genType = 0
//        commonData.tag = "hihi"
//        commonData.template = hashMapOf(
//
//            Pair("title", "i am title"),
//            Pair("taskType", "Img2VideoByPoseTask"),
//            Pair("templatePreviewUrl", "https://cdn.fancytool.org/cdn/f/20250805/jv1754372563017.mp4"),
//            Pair("isVideoPreview", true),
//            Pair("templateName", "38321761463618"),
//            Pair("isFpsSize", true),
//            Pair("size1", "15"),
//            Pair("size2", "25"),
//            Pair("credits1", 5),
//            Pair("credits2", 9),
//        )
//        hotDataList.add(commonData)
//
//        val clothData = UserConfigDataModel()
//        clothData.title = "Clothes Swap"
//        clothData.description = "Upload photos to start making"
//        clothData.imgUrl = "https://cdn.fancytool.org/cdn/c/jv1951965964090875904.png"
//        clothData.genType = 3
//        clothData.tag = "cloth"
//        hotDataList.add(clothData)
//
//        val clothData1 = UserConfigDataModel()
//        clothData1.title = "Clothes Swap with template"
//        clothData1.description = "Upload photos to start making"
//        clothData1.imgUrl = "https://cdn.fancytool.org/cdn/c/jv1951965964090875904.png"
//        clothData1.genType = 3
//        clothData1.tag = "cloth"
//        clothData1.template = hashMapOf(
//            Pair("templateUrl", "https://cdn.fancytool.org/cdn/c/jv1951965964090875904.png"),
//            Pair("templateName", "31993898713954")
//        )
//        hotDataList.add(clothData1)
//
//        val faceSwapData = UserConfigDataModel()
//        faceSwapData.title = "Face Swap"
//        faceSwapData.description = "Upload photos to start making"
//        faceSwapData.imgUrl = "https://cdn.fancytool.org/cdn/template_image/20250412/ai_admin_1744455312282.png"
//        faceSwapData.genType = 1
//        faceSwapData.tag = "face"
//        hotDataList.add(faceSwapData)
//
//        val faceSwapData1 = UserConfigDataModel()
//        faceSwapData1.title = "Face Swap with template"
//        faceSwapData1.description = "Upload photos to start making"
//        faceSwapData1.imgUrl = "https://cdn.fancytool.org/cdn/template_image/20250412/ai_admin_1744455312282.png"
//        faceSwapData1.genType = 1
//        faceSwapData1.tag = "face"
//        faceSwapData1.template = hashMapOf(
//            Pair("templateUrl", "https://cdn.fancytool.org/cdn/template_image/20250412/ai_admin_1744455312282.png"),
//            Pair("templateName", "18011180321025"),
//            Pair("headPos", "391,128,571,357"),
//
//        )
//        hotDataList.add(faceSwapData1)
//
//        val videoFaceSwapData = UserConfigDataModel()
//        videoFaceSwapData.title = "Video Face Swap"
//        videoFaceSwapData.description = "Upload photos to start making"
//        videoFaceSwapData.imgUrl = "https://cdn.fancytool.org/cdn/f/20250805/jv1754372568551.jpeg"
//        videoFaceSwapData.genType = 2
//        videoFaceSwapData.tag = "video"
//        hotDataList.add(videoFaceSwapData)
//
//        val videoFaceSwapData1 = UserConfigDataModel()
//        videoFaceSwapData1.title = "Video Face Swap with template"
//        videoFaceSwapData1.description = "Upload photos to start making"
//        videoFaceSwapData1.imgUrl = "https://cdn.fancytool.org/cdn/f/20250805/jv1754372568551.jpeg"
//        videoFaceSwapData1.genType = 2
//        videoFaceSwapData1.tag = "video"
//        videoFaceSwapData1.template = hashMapOf(
//            Pair("templateUrl", "https://cdn.fancytool.org/cdn/f/20250805/jv1754372563017.mp4"),
//            Pair("templateName", "38321761463618"),
//            Pair("templateFaceUrl", "https://cdn.fancytool.org/hormony/template_video/20250805/jv1754372563017.jpg"),
//            Pair("credits", 5),
//
//            )
//        hotDataList.add(videoFaceSwapData1)
//
//        val clayStyleSwapData = UserConfigDataModel()
//        clayStyleSwapData.title = "Clay Style"
//        clayStyleSwapData.description = "Upload photos to start making"
//        clayStyleSwapData.genType = 4
//        clayStyleSwapData.tag = "other"
//        hotDataList.add(clayStyleSwapData)
//
//        print(
//            Gson().newBuilder()
//            .setPrettyPrinting()
//            .create()
//            .toJson(hotDataList))
//
//        mViewModel.uiStateData.value.hotTabData.configList = hotDataList

    }

    override fun onBackPressed() {
        super.onBackPressed()
        moveTaskToBack(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //提交归因
        ReferrerUtil.submitGoogleReferrer(this)
        ReferrerUtil.submitFacebookReferrer(this)
    }

    override fun onResume() {
        super.onResume()
        AppEventsLogger.activateApp(application)
    }
}

@Composable
fun HomeScreen(
    homeActivity: HomeActivity,
    uiStateData: HomeActivityData
) {
    val navController = rememberNavController()

    // 监听导航状态变化来更新选中的 tab
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    var selectedTab by remember { mutableIntStateOf(0) }

    // 根据当前路由更新选中的 tab
    LaunchedEffect(currentRoute) {
        selectedTab = when (currentRoute) {
            "hot" -> 0
//            "dance" -> 1
            "clothes swap" -> 1
            "swap" -> 2
            else -> 0
        }
    }

    // 确保应用启动时正确设置初始状态
    LaunchedEffect(Unit) {
        if (currentRoute == null) {
            selectedTab = 0
        }
    }

    AiSwapTheme {

        Box {
            Scaffold(
                topBar = {
                    TopBar(
                        userAvatarUrl = uiStateData.avatarUrl,
                        credits = uiStateData.credits.toString()
                    )
                },
                bottomBar = {
                    BottomNavBar(selectedTab = selectedTab, onTabSelected = { index ->
                        when (index) {
                            3 -> {
                                AppRouterManager.enterPurchaseActivity()
                            }

                            else -> {
                                // 导航到对应的 tab，保持状态
                                val route = when (index) {
                                    0 -> "hot"
//                                    1 -> "dance"
                                    1 -> "clothes swap"
                                    2 -> "swap"
                                    else -> "hot"
                                }

                                navController.navigate(route) {
                                    // 弹出到根目录，但保持状态
                                    popUpTo(navController.graph.startDestinationId) {
                                        saveState = true
                                    }
                                    // 避免创建多个相同的目的地
                                    launchSingleTop = true
                                    // 恢复状态
                                    restoreState = true
                                }
                            }
                        }
                    })
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "hot"
                ) {
                    composable("hot") {
                        HotTab(innerPadding, homeActivity, onTabRequest = { tab ->
                            navController.navigate(tab) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        })
                    }
//                    composable("dance") {
//                        DanceTab(innerPadding, homeActivity)
//                    }
                    composable("clothes swap") {
                        ClothesSwapTab(innerPadding, homeActivity)
                    }
                    composable("swap") {
                        SwapTab(innerPadding, homeActivity)
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    userAvatarUrl: String? = null,
    credits: String = "0"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 24.dp, vertical = 12.dp)
            .padding(WindowInsets.statusBars.asPaddingValues()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val avatarModifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable {
                AppRouterManager.enterMineActivity()
            }

        if (!userAvatarUrl.isNullOrBlank() && userAvatarUrl.startsWith("http")) {
            AsyncImage(
                model = userAvatarUrl,
                contentDescription = "Profile",
                modifier = avatarModifier,
                placeholder = painterResource(id = R.drawable.ic_profile_default),
                error = painterResource(id = R.drawable.ic_profile_default)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.ic_profile_default),
                contentDescription = "Profile",
                modifier = avatarModifier,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${credits} credits",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(end = 16.dp)
                .clickable { }
        )
    }
}

@Composable
fun BottomNavBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val items = listOf("Hot", "Clothes\nSwap", "Swap", "Purchase")
    val icons = listOf(
        R.drawable.ic_hot,
        R.drawable.ic_dance,
        R.drawable.ic_swap,
        R.drawable.ic_purchase
    )

    NavigationBar(
        containerColor = Color.Black,
        modifier = Modifier.fillMaxWidth().height(120.dp)
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    if (isSelected) {
                        Text(
                            text = item,
                            fontWeight = FontWeight.Bold,
                            style = androidx.compose.ui.text.TextStyle(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFF9D9FE7),
                                        Color(0xFF9DE7A6)
                                    )
                                )
                            ),
                            textAlign = TextAlign.Center
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = icons[index]),
                            contentDescription = item,
                            tint = Color.Gray
                        )
                    }
                },
                label = null,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}
