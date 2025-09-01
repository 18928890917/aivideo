package com.stylehub.aivideo.ui.settings

import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.ui.dialog.DevDialog
import com.stylehub.aivideo.utils.AppRouterManager

class SettingsActivity : BaseActivity<SettingsActivityViewModel, SettingsActivityUiData>() {
    override val mViewModel: SettingsActivityViewModel by viewModels()

    @Composable
    override fun ProvideContent(uiStateData: SettingsActivityUiData) {

        SettingsScreen(
            uiStateData = uiStateData,
            onBack = { finish() },
            onLoginClick = {
                mViewModel.login()
            },
            onLogoutClick = {
                mViewModel.logout()
            },
            onPrivacyPolicyClick = {
                AppRouterManager.enterSystemBroseClient(
                    getString(R.string.privacy_url)
                )
            },
            onTermOfUseClick = {
                AppRouterManager.enterSystemBroseClient(
                    getString(R.string.user_policy_url)
                )
            }
        )

        DevDialog(
            uiStateData.showDevDialog,
            {mViewModel.dismissDevDialog()}
        )
    }
}

@Composable
fun SettingsScreen(
    uiStateData: SettingsActivityUiData,
    onBack: () -> Unit,
    onLoginClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermOfUseClick: () -> Unit
) {

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
                text = "Settings",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(Modifier.height(60.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 5.dp)) {
            SettingsItem(
                iconRes = R.drawable.ic_privacy_policy,
                value = "Privacy Policy",
                onClick = onPrivacyPolicyClick
            )
            Spacer(Modifier.height(20.dp))

            SettingsItem(
                iconRes = R.drawable.ic_term_of_use,
                value = "Term of Use",
                onClick = onTermOfUseClick
            )
            Spacer(Modifier.height(20.dp))

            SettingsItem(
                title = "About AIPlay",
                value = uiStateData.versionName,
                onClick = {}
            )
            Spacer(Modifier.height(20.dp))

            SettingsItem(
                title = "Contact Support",
                value = uiStateData.contactUrl,
                onClick = {}
            )
        }

        Spacer(Modifier.weight(1f))

        ActionButton(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = if (uiStateData.isLogin) "Log out" else "Log in",
            onClick = if (uiStateData.isLogin) onLogoutClick else onLoginClick
        )
    }
}

@Composable
fun SettingsItem(
    iconRes: Int? = null,
    title: String? = null,
    value: String? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .border(1.dp, Color.Gray.copy(alpha = 0.5f), RoundedCornerShape(50))
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        iconRes?.let {
            Icon(painter = painterResource(it), contentDescription = title, tint = Color.White)
        }

        title?.let {
            Text(text = it, color = Color.White, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.weight(1f))

        if (value != null) {

            Text(
                text = value,
                color = if (title == null) Color.White else Color(0xFF5A5B5B),
                fontSize = 13.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ActionButton(modifier: Modifier = Modifier, text: String, onClick: () -> Unit) {
    val gradientBrush = Brush.horizontalGradient(
        colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
    )
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .border(
                width = 1.dp,
                brush = gradientBrush,
                shape = RoundedCornerShape(10)
            ),
        shape = RoundedCornerShape(10),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
    ) {
        Text(text, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}