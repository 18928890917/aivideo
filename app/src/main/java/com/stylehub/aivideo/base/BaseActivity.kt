package com.stylehub.aivideo.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.stylehub.aivideo.R
import com.stylehub.aivideo.ui.dialog.BottomHintMessageDialog
import com.stylehub.aivideo.ui.dialog.CommonLoadingDialog
import com.stylehub.aivideo.ui.dialog.GoogleLoginDialog
import com.stylehub.aivideo.ui.dialog.NetworkOfflineDialog
import com.stylehub.aivideo.utils.AppRouterManager
import com.stylehub.aivideo.utils.GoogleSignInHelper
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.ToastUtil

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
abstract class BaseActivity<VM : BaseViewModel<T>, T : Any> : ComponentActivity() {
    abstract val mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 全屏适配
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        //设置状态栏背景颜色为黑色
        window.statusBarColor = 0xFF111212.toInt()
        window.navigationBarColor = 0xFF111212.toInt()
        // 使用兼容库来设置状态栏文字颜色为白色（浅色模式关闭 => 文字变白）
        val controller = WindowInsetsControllerCompat(window, window.decorView)
        controller.isAppearanceLightStatusBars = false // false 表示使用浅色文字（白色）

        mViewModel.attach(this)
        LoginManager.googleSignInHelper = GoogleSignInHelper(this)

        setContent {
            // 调用子类提供的 Composable 函数
            ContentView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.detach(this)
    }

    override fun onBackPressed() {
        if (!mViewModel.onBackPressed()) {
            super.onBackPressed()
        }
    }

    @Composable
    protected fun ContentView() {
        val uiStateData by mViewModel.uiStateData.collectAsStateWithLifecycle()
        ProvideContent(uiStateData)
        NetworkOfflineDialog(mViewModel.networkOffline) { mViewModel.networkOffline = false }

        GoogleLoginDialog(
            show = mViewModel.showLoginDialog,
            onDismiss = { mViewModel.dismissLoginDialog() },
            onGoogleLogin = {
                LoginManager.googleLogin(
                    onSuccess = {
                        mViewModel.onLoginSuccess()
                        ToastUtil.show("Login success")
                    },
                    onError = { _,
                                msg ->

                        mViewModel.dismissLoginDialog()
                        mViewModel.showMessageHintDialog(msg, "Error")
                    }
                )
            },
            onTermsClick = {
                //使用条款
                AppRouterManager.enterSystemBroseClient(
                    getString(R.string.user_policy_url)
                )
            },
            onPrivacyClick = {
                //隐私协议
                AppRouterManager.enterSystemBroseClient(
                    getString(R.string.privacy_url)
                )
            }
        )

        CommonLoadingDialog(
            show = mViewModel.showLoadingDialog,
            text = mViewModel.loadingDialogTextHint
        )

        //底部提示弹窗
        BottomHintMessageDialog(
            show = mViewModel.showMessageHintDialog,
            onDismiss = { mViewModel.dismissMessageHintDialog() },
            hint = mViewModel.messageHintDialogHint,
            title = mViewModel.messageHintDialogTitle
        )
    }

    @Composable
    protected abstract fun ProvideContent(uiStateData: T)
}