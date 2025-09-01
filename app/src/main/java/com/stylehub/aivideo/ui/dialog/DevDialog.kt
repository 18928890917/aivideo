package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.gson.Gson
import com.stylehub.aivideo.constants.PrefKey
import com.stylehub.aivideo.ui.common.topRoundedBorder
import com.stylehub.aivideo.utils.AppUtil
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.SharedPreferenceUtil

/**
 *
 * Create by league at 2025/7/13
 *
 * Write some description here
 */

@Composable
fun DevDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String) -> Unit = { _, _, _ -> }
) {

    CommonBottomDialog(
        show = show,
        onDismiss = onDismiss
    ) {

        var deviceId by remember { mutableStateOf(AppUtil.getDeviceId()) }
        var userId by remember { mutableStateOf(LoginManager.getUserId()?:"") }
        var userToken by remember { mutableStateOf(LoginManager.getUserToken()?:"") }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 240.dp
                )
                .background(
                    Color(0xFF111212),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .topRoundedBorder(Color(0x20FFFFFF), cornerRadius = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "开发者设置",
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // 输入框1，可以输入设备id
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "设备ID",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    value = deviceId,
                    onValueChange = { deviceId = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // 输入框2，可以输入用户id
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "用户ID",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    value = userId,
                    onValueChange = { userId = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.padding(16.dp))

            // 输入框3，可以输入用户token
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "用户Token",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                BasicTextField(
                    value = userToken,
                    onValueChange = { userToken = it },
                    textStyle = TextStyle(color = Color.White, fontSize = 14.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF2A2A2A), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }

            Spacer(modifier = Modifier.padding(24.dp))

            // 底部横向的取消/确认按钮
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { it.invoke() },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5A5B5B))
                ) {
                    Text("取消", color = Color.White)
                }

                Button(
                    onClick = {

                        it.invoke()

                        SharedPreferenceUtil.put(PrefKey.DEVICE_ID, deviceId)

                        val currentUserInfo = LoginManager.getUser()
                        val PREF_KEY_USER = "fast_login_model"
                        currentUserInfo?.let {
                            it.userId = userId
                            it.userToken = userToken
                            SharedPreferenceUtil.put(PREF_KEY_USER, Gson().toJson(it))
                        }
                        onConfirm(deviceId, userId, userToken)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8A2BE2))
                ) {
                    Text("确认", color = Color.White)
                }
            }
        }
    }
}