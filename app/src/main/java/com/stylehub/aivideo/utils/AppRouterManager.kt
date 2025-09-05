package com.stylehub.aivideo.utils

import android.content.Context
import android.content.Intent
import com.stylehub.aivideo.AiSwapApplication
import com.stylehub.aivideo.base.BaseActivity
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CreateImageTaskTemplateModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImg2VideoPoseTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.ui.common.WebViewActivity
import com.stylehub.aivideo.ui.mine.MineActivity
import com.stylehub.aivideo.ui.purchase.PurchaseActivity
import com.stylehub.aivideo.ui.settings.SettingsActivity
import com.stylehub.aivideo.ui.template.ClayStylePhotoSwapActivity
import com.stylehub.aivideo.ui.template.ClothesSwapActivity
import com.stylehub.aivideo.ui.template.CommonSwapActivity
import com.stylehub.aivideo.ui.template.DancePhotoSwapActivity
import com.stylehub.aivideo.ui.template.FacePhotoSwapActivity
import com.stylehub.aivideo.ui.template.ImageFaceSwapActivity
import com.stylehub.aivideo.ui.template.VideoFaceSwapActivity
import java.io.Serializable

/**
 *
 * Create by league at 2025/7/3
 *
 * app路由管理工具
 */
object AppRouterManager {

    private fun getContext(): Context {
        return AiSwapApplication.getInstance().getTopActivity()?: AiSwapApplication.getInstance()
    }

    private fun showLoginDialog() {

        if (getContext() is BaseActivity<*, *>) {
            ((getContext() as BaseActivity<*, *>).mViewModel as BaseViewModel<*>).showLoginDialog()
        }
    }

    /**
     * 进入设置页
     */
    fun enterSettingsActivity(context: Context = getContext()) {
        context.startActivity(Intent(context, SettingsActivity::class.java))
    }

    /**
     * 进入我的页面
     */
    fun enterMineActivity(context: Context = getContext()) {
        context.startActivity(Intent(context, MineActivity::class.java))
    }

    /**
     * 进入充值页面
     */
    fun enterPurchaseActivity(context: Context = getContext()) {
        context.startActivity(Intent(context, PurchaseActivity::class.java))
    }

    /**
     * 进入图片换脸页面
     */
    fun enterFacePhotoSwapActivity(context: Context = getContext(), template: Template) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, FacePhotoSwapActivity::class.java)
        intent.putExtra(FacePhotoSwapActivity.EXTRA_TEMPLATE, template as Serializable)
        context.startActivity(intent)
    }

    /**
     * 进入图片换脸页面
     */
    fun enterImageFaceSwapActivity(context: Context = getContext(), template: GetImageTemplateRespDataModel) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, ImageFaceSwapActivity::class.java)
        intent.putExtra(ImageFaceSwapActivity.EXTRA_TEMPLATE, template as Serializable)
        context.startActivity(intent)
    }

    /**
     * 进入视频换脸页面
     */
    fun enterVideoFaceSwapActivity(context: Context = getContext(), template: FaceSwapVideoTemplateRespDataModel) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, VideoFaceSwapActivity::class.java)
        intent.putExtra(VideoFaceSwapActivity.EXTRA_TEMPLATE, template as Serializable)
        context.startActivity(intent)
    }

    /**
     * 进入换衣页面
     */
    fun enterClothesSwapActivity(context: Context = getContext(), template: ClothesTemplateRespDataModel) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, ClothesSwapActivity::class.java)
        intent.putExtra(ClothesSwapActivity.EXTRA_TEMPLATE, template as Serializable)
        context.startActivity(intent)
    }

    /**
     * 进入舞蹈换脸页面
     */
    fun enterDancePhotoSwapActivity(context: Context = getContext(), template: GetImg2VideoPoseTemplateRespDataModel) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, DancePhotoSwapActivity::class.java)
        intent.putExtra(DancePhotoSwapActivity.EXTRA_TEMPLATE, template)
        context.startActivity(intent)
    }

    /**
     * 进入黏土风格换脸页面
     */
    fun enterClayStylePhotoSwapActivity(context: Context = getContext()) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, ClayStylePhotoSwapActivity::class.java)
        context.startActivity(intent)
    }

    /**
     * 进入通用换图页面
     */
    fun enterCommonSwapActivity(context: Context = getContext(), template: CreateImageTaskTemplateModel) {

        if (!LoginManager.isGoogleLogin()) {
            showLoginDialog()
            return
        }

        val intent = Intent(context, CommonSwapActivity::class.java)
        intent.putExtra(CommonSwapActivity.EXTRA_TEMPLATE, template)
        context.startActivity(intent)
    }


    /**
     * 进入WebView页面
     */
    fun enterWebViewActivity(url: String, context: Context = getContext()) {
        val intent = Intent(context, WebViewActivity::class.java)
        intent.putExtra(WebViewActivity.EXTRA_URL, url)
        context.startActivity(intent)
    }

    /**
     * 打开系统浏览器
     */
    fun enterSystemBroseClient(url: String, context: Context = getContext()) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = android.net.Uri.parse(url)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}