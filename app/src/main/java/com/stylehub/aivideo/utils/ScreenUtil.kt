package com.stylehub.aivideo.utils

import android.app.Activity
import android.app.Application
import android.content.res.Resources
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager

object ScreenUtil {
    private var screenWidth: Int = 0
    private var screenHeight: Int = 0
    private var statusBarHeight: Int = 0
    private var navigationBarHeight: Int = 0
    private var safeAreaHeight: Int = 0

    fun init(app: Application) {
        val resources = app.resources
        val displayMetrics = resources.displayMetrics
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        // 获取状态栏高度
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        statusBarHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0

        // 获取导航栏高度
        val navId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        navigationBarHeight = if (navId > 0) resources.getDimensionPixelSize(navId) else 0

        // 计算安全区域高度（屏幕高度-状态栏-导航栏）
        safeAreaHeight = screenHeight - statusBarHeight - navigationBarHeight
    }

    /** 获取屏幕宽度（像素） */
    fun getScreenWidth(): Int = screenWidth

    /** 获取屏幕高度（像素） */
    fun getScreenHeight(): Int = screenHeight

    /** 获取顶部危险区域高度（状态栏高度，像素） */
    fun getTopSafeInset(): Int = statusBarHeight

    /** 获取底部危险区域高度（导航栏高度，像素） */
    fun getBottomSafeInset(): Int = navigationBarHeight

    /** 获取屏幕安全区域高度（像素） */
    fun getSafeAreaHeight(): Int = safeAreaHeight

    /** px转dp Int扩展 */
    fun Int.px2dp(): Float = this / Resources.getSystem().displayMetrics.density
    /** px转dp Float扩展 */
    fun Float.px2dp(): Float = this / Resources.getSystem().displayMetrics.density

    /** px转sp Int扩展 */
    fun Int.px2sp(): Float = this / Resources.getSystem().displayMetrics.scaledDensity
    /** px转sp Float扩展 */
    fun Float.px2sp(): Float = this / Resources.getSystem().displayMetrics.scaledDensity

    /** dp转px Int扩展（全局） */
    fun Int.dp2px(): Int = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    /** dp转px Float扩展（全局） */
    fun Float.dp2px(): Int = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()
    /** sp转px Int扩展（全局） */
    fun Int.sp2px(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()
    /** sp转px Float扩展（全局） */
    fun Float.sp2px(): Int = (this * Resources.getSystem().displayMetrics.scaledDensity + 0.5f).toInt()

    /**
     * 设置成全屏
     *
     * @param activity 页面
     */
    fun setFullScreen(activity: Activity?) {
        if (activity == null) return

        try {
            val systemUiOption = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or  //内容与导航栏重叠
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or  //隐藏底部导航栏
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY //沉浸式


            activity.window.decorView.systemUiVisibility = systemUiOption
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val lp = activity.window.attributes
                lp.layoutInDisplayCutoutMode =
                    WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                activity.window.attributes = lp
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * 布局覆盖到statusBar
     * @param activity  页面实例
     */
    fun expandToStatusBar(activity: Activity?) {
        if (activity == null) return

        try {
            //4.4+

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                val window = activity.window
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

                //5.0+
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.statusBarColor = 0x00000000
                }

                val contentView = window.findViewById<ViewGroup>(Window.ID_ANDROID_CONTENT)
                //获取我们自己画的父布局
                val mContentChild = contentView.getChildAt(0)


                if (mContentChild != null) {
                    mContentChild.systemUiVisibility =
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                    //                    mContentChild.setPadding(mContentChild.getPaddingLeft(), mContentChild.getPaddingTop() + getStatusBarHeight(activity), mContentChild.getPaddingRight(), mContentChild.getPaddingBottom());
                }
            }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
} 