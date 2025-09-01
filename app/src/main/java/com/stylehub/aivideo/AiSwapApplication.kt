package com.stylehub.aivideo

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.stylehub.aivideo.utils.AppUtil
import com.stylehub.aivideo.utils.ScreenUtil
import com.stylehub.aivideo.utils.SharedPreferenceUtil
import java.lang.ref.WeakReference
import kotlin.reflect.KClass
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache

/**
 *
 * Create by league at 2025/6/19
 *
 * Write some description here
 */
class AiSwapApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        g_instance = this

        //初始化
        init()
        SharedPreferenceUtil.init(this)
        AppUtil.getDeviceId()

    }

    private val mActivityList: MutableList<Activity> = mutableListOf()
    private val mCurrentActiveActivityList: MutableList<Activity> = mutableListOf()
    private val mAppStateChangeListenerMap: MutableMap<AppStateListener, WeakReference<Context?>> =
        mutableMapOf()


    interface AppStateListener {
        fun onAppForeground()
        fun onAppBackground()
    }

    companion object {

        private lateinit var g_instance: AiSwapApplication

        @JvmStatic
        fun getInstance(): AiSwapApplication {
            return g_instance
        }
    }

    private fun init() {

        ScreenUtil.init(this)
        initActivityTask()

        // 全局配置 Coil 磁盘缓存
        val imageLoader = ImageLoader.Builder(this)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache")) // 自定义缓存目录
                    .maxSizeBytes(512L * 1024 * 1024) // 512MB
                    .build()
            }
            .build()
        Coil.setImageLoader(imageLoader)
    }

    private fun initActivityTask() {

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                mActivityList.add(activity)
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {

                if (!mCurrentActiveActivityList.contains(activity)) {
                    mCurrentActiveActivityList.add(activity)

                    if (mCurrentActiveActivityList.size == 1) {
                        onAppForeground()
                    }
                }
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {

                if (mCurrentActiveActivityList.contains(activity)) {
                    mCurrentActiveActivityList.remove(activity)

                    if (mCurrentActiveActivityList.size == 0) {
                        onAppBackground()
                    }
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
                mActivityList.remove(activity)
            }

        })
    }

    fun getTopActivity(): Activity? {
        if (mActivityList.size > 0) {
            return mActivityList[mActivityList.size - 1]
        }
        return null
    }

    fun finishTopActivity() {

        val topAct = getTopActivity()
        topAct?.finish()
    }

    fun finishParticularActivity(clsName: String?) {

        if (clsName == null || mActivityList.size == 0) {
            return
        }

        mActivityList.forEach {
            val currentActName = it::class.qualifiedName
            if (clsName == currentActName) {
                it.finish()
            }
        }
    }

    fun <T : Activity> finishParticularActivity(cls: KClass<T>) {

        finishParticularActivity(cls.qualifiedName)
    }

    fun finishAllActivity() {

        mActivityList.forEach {
            it.finish()
        }
    }

    fun <T : Activity> getParticularActivity(clsName: String?): T? {

        if (clsName == null || mActivityList.size == 0) {
            return null
        }

        for (act in mActivityList.reversed()) {
            val actName = act::class.qualifiedName
            if (actName == clsName) {
                return act as T
            }
        }
        return null
    }

    fun <T : Activity> getParticularActivity(cls: KClass<T>): T? {

        return getParticularActivity(cls.qualifiedName)
    }

    private fun onAppForeground() {

        val needRemove = mutableListOf<AppStateListener>()
        mAppStateChangeListenerMap.forEach {
            if (it.value.get() != null) {
                it.key.onAppForeground()
            } else {
                needRemove.add(it.key)
            }
        }
        needRemove.forEach {
            mAppStateChangeListenerMap.remove(it)
        }
    }

    private fun onAppBackground() {

        val needRemove = mutableListOf<AppStateListener>()
        mAppStateChangeListenerMap.forEach {
            if (it.value.get() != null) {
                it.key.onAppBackground()
            } else {
                needRemove.add(it.key)
            }
        }
        needRemove.forEach {
            mAppStateChangeListenerMap.remove(it)
        }
    }

    fun addAppStateChangeListener(
        listener: AppStateListener,
        removeAfterActivityFinished: Boolean = true
    ) {

        if (!mAppStateChangeListenerMap.keys.contains(listener)) {
            mAppStateChangeListenerMap.put(
                listener,
                WeakReference(if (removeAfterActivityFinished) getTopActivity() else this)
            )
        }
    }

    fun removeAppStateChangeListener(listener: AppStateListener) {

        if (mAppStateChangeListenerMap.keys.contains(listener))
            mAppStateChangeListenerMap.remove(listener)
    }
}