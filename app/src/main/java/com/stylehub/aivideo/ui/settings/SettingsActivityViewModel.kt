package com.stylehub.aivideo.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.utils.AppUtil
import com.stylehub.aivideo.utils.LoginManager

/**
 *
 * Create by league at 2025/7/1
 *
 */

class SettingsActivityUiData {

    var versionName by mutableStateOf("")
    var contactUrl by mutableStateOf("")
    var isLogin by mutableStateOf(false)
    var showDevDialog by mutableStateOf(false)
}

class SettingsActivityViewModel :BaseViewModel<SettingsActivityUiData>(SettingsActivityUiData()) {

    private val mutableData = _uiStateData.value

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateInfo()

        LoginManager.updateUserAccount {
            updateInfo()
        }
    }

    private fun updateInfo() {

        mutableData.isLogin = LoginManager.isGoogleLogin()
        mutableData.versionName = "Version ${AppUtil.getVersionName()}"
        mutableData.contactUrl = LoginManager.getEmail()?:"xxx.com"
    }

    fun login() {
        showLoginDialog()
    }

    fun logout() {
        LoginManager.logout()
        updateInfo()
    }

    fun showDevDialog() {
        mutableData.showDevDialog = true
    }
    fun dismissDevDialog() {
        mutableData.showDevDialog = false
    }

    override fun onLoginSuccess() {
        updateInfo()
    }

}