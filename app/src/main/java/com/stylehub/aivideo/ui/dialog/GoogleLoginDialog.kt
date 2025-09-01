package com.stylehub.aivideo.ui.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.stylehub.aivideo.R
import com.stylehub.aivideo.ui.common.topRoundedBorder
import com.stylehub.aivideo.utils.ScreenUtil.px2dp

@Composable
fun GoogleLoginDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onGoogleLogin: () -> Unit,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    CommonBottomDialog(
        show = show,
        onDismiss = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .background(
                    Color(0xFF111212),
                    RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)
                )
                .topRoundedBorder(Color(0x20FFFFFF), cornerRadius = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                IconButton(
                    onClick = { it.invoke() },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                }
            }
            Spacer(Modifier.height(10.dp))

            Box {
                var textWidth by remember { mutableFloatStateOf(0f) }
                var textHeight by remember { mutableFloatStateOf(0f) }

                Text(
                    modifier = Modifier.onSizeChanged {
                        textWidth = it.width.px2dp()
                        textHeight = it.height.px2dp()
                    },
                    text = "Sign up",
                    fontSize = 45.sp,
                    fontStyle = FontStyle.Italic,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier.matchParentSize().padding(
                        start = (69f * textWidth / 144f).dp,
                        top = (textHeight * 8 / 9f).dp
                    )
                ) {
                    Spacer(
                        modifier = Modifier
                            .width(48.dp)
                            .height(3.dp)
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                                ),
                                shape = RoundedCornerShape(10.dp)
                            )

                        )
                }

            }

            Spacer(Modifier.height(30.dp))

            Text(
                text = "After successful sign up",
                color = Color.White,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            val annotatedText = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                    )
                )) {
                    // 默认样式
                    append("Get ")
                    // 粗体样式
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                        append("2 Credits")
                    }
                    append(" for Free")
                }
            }

            Text(
                text = annotatedText,
                fontSize = 13.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(32.dp))

            GoogleLoginButton(onClick = {
                it.invoke()
                onGoogleLogin()
            })

            Spacer(Modifier.height(58.dp))

            Row(
                Modifier.fillMaxWidth(0.6f),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Terms of Use",
                    fontSize = 13.sp,
                    modifier = Modifier.clickable(onClick = onTermsClick),
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        )
                    )
                )
                Text(
                    text = "  |  ",
                    color = Color(0xFF2B2B2B),
                    fontSize = 13.sp
                )
                Text(
                    text = "Privacy Policy",
                    fontSize = 13.sp,
                    modifier = Modifier.clickable(onClick = onPrivacyClick),
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF9D9FE7), Color(0xFF9DE7A6))
                        )
                    )
                )
            }

            Spacer(Modifier.height(35.dp))
        }

    }
}

@Composable
fun GoogleLoginButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {

        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(R.mipmap.bg_google_login_btn),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )

        Box(modifier = Modifier.padding(start = 22.dp, top = 5.dp).align(Alignment.CenterStart)) {
            Image(
                modifier = Modifier.size(30.dp),
                painter = painterResource(R.mipmap.ic_google),
                contentDescription = "",
                contentScale = ContentScale.FillWidth
            )
        }

        Text(
            text = "Continue with Google",
            color = Color.Black,
            fontWeight = FontWeight.Black,
            fontSize = 13.sp
        )
    }
} 