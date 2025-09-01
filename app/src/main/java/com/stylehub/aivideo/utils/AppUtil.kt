package com.stylehub.aivideo.utils

import android.annotation.SuppressLint
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.provider.Settings
import com.stylehub.aivideo.AiSwapApplication
import com.stylehub.aivideo.constants.PrefKey

object AppUtil {
    /**
     * 获取设备唯一ID（Android ID），优先从SharedPreference获取，无则存储
     */
    @SuppressLint("HardwareIds")
    fun getDeviceId(): String {
        val cached = SharedPreferenceUtil.get<String>(PrefKey.DEVICE_ID, null)
        if (!cached.isNullOrEmpty()) return cached
        val deviceId = Settings.Secure.getString(
            AiSwapApplication.getInstance().contentResolver,
            Settings.Secure.ANDROID_ID
        ) ?: ""
        if (deviceId.isNotEmpty()) {
            SharedPreferenceUtil.put(PrefKey.DEVICE_ID, deviceId)
        }
        return deviceId
    }

    fun getVersionName(): String {
        try {
            val packageInfo: PackageInfo =
                AiSwapApplication.getInstance().getPackageManager()
                    .getPackageInfo(AiSwapApplication.getInstance().getPackageName(), 0)
            return packageInfo.versionName ?: ""
        } catch (e: PackageManager.NameNotFoundException) {
            return ""
        }
    }

} 