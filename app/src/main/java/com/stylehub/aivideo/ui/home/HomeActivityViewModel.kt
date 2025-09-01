package com.stylehub.aivideo.ui.home

import android.text.TextUtils
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.common.util.CollectionUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.stylehub.aivideo.base.BaseViewModel
import com.stylehub.aivideo.network.ApiService
import com.stylehub.aivideo.network.Network
import com.stylehub.aivideo.network.model.out.ClothesTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.CommonRespModel
import com.stylehub.aivideo.network.model.out.FaceSwapVideoTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImageTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.GetImg2VideoPoseTemplateRespDataModel
import com.stylehub.aivideo.network.model.out.Template
import com.stylehub.aivideo.network.model.out.UserCommonInfoRespDataModel
import com.stylehub.aivideo.network.model.out.UserConfigDataModel
import com.stylehub.aivideo.utils.LoginManager
import com.stylehub.aivideo.utils.ToastUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */

enum class SwapTabsEnum(val title: String) {
    PHOTO("Photo Faceswap"),
    VIDEO("Video Faceswap"),
    CLOTHES("Clothes Swap")
    ;

    companion object {

        @JvmStatic
        fun of(index: Int): SwapTabsEnum {
            return when(index) {
                1 -> VIDEO
                2 -> CLOTHES
                else -> PHOTO
            }
        }
    }
}

class HomeActivityData {

    /**
     * 头像
     */
    var avatarUrl by mutableStateOf("")

    /**
     * 点数
     */
    var credits by mutableStateOf(0)

    /**
     * 换脸数据模型
     */
    var swapTabData: SwapTabData = SwapTabData()

    /**
     * 热门tab模型
     */
    var hotTabData : HotTabData = HotTabData()

    /**
     * 视频动作tab模型
     */
    var danceTabData: DanceTabData = DanceTabData()

}

class HotTabData {

    var configList by mutableStateOf<List<UserConfigDataModel>>(emptyList())

    /**
     * 当前loading状态
     */
    var isLoading by mutableStateOf(false)

}

class DanceTabData {

    var img2VideoPoseTemplates by mutableStateOf<List<GetImg2VideoPoseTemplateRespDataModel>>(emptyList())
    
    /**
     * 当前loading状态
     */
    var isImg2VideoPoseTemplateLoading by mutableStateOf(false)
}

class SwapTabData {

    /**
     * 换脸模板
     */
    var imageTemplates by mutableStateOf<List<GetImageTemplateRespDataModel>>(emptyList())

    /**
     * 视频换脸模板
     */
    var videoTemplates by mutableStateOf<List<FaceSwapVideoTemplateRespDataModel>>(emptyList())

    /**
     * 换衣模板
     */
    var clothesTemplates by mutableStateOf<List<ClothesTemplateRespDataModel>>(emptyList())

    /**
     * 当前所选的tab
     */
    var currentSelectTab by mutableStateOf(SwapTabsEnum.PHOTO)

    /**
     * 当前loading状态
     */
    var isImageTemplateLoading by mutableStateOf(false)

    /**
     * 当前loading状态
     */
    var isVideoTemplateLoading by mutableStateOf(false)

    /**
     * 当前loading状态
     */
    var isClothesTemplateLoading by mutableStateOf(false)

}

class HomeActivityViewModel : BaseViewModel<HomeActivityData>(HomeActivityData()) {
    
    private var homeData = _uiStateData.value
    private var swapTabData = homeData.swapTabData
    private var hotTabData = homeData.hotTabData
    private var danceTabData = homeData.danceTabData

    private fun updateUserInfo() {
        homeData.avatarUrl = LoginManager.getAvatar()?: ""
        homeData.credits = LoginManager.getCredit()
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        updateUserInfo()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        updateUserInfo()
    }

    fun isCurrentTabLoading(): Boolean {

        return when(swapTabData.currentSelectTab) {

            SwapTabsEnum.PHOTO -> swapTabData.isImageTemplateLoading
            SwapTabsEnum.VIDEO -> swapTabData.isVideoTemplateLoading
            SwapTabsEnum.CLOTHES -> swapTabData.isClothesTemplateLoading
        }
    }

    fun isCurrentTabNoData(): Boolean {

        return when(swapTabData.currentSelectTab) {

            SwapTabsEnum.PHOTO -> !swapTabData.isImageTemplateLoading && CollectionUtils.isEmpty(swapTabData.imageTemplates)
            SwapTabsEnum.VIDEO -> !swapTabData.isVideoTemplateLoading && CollectionUtils.isEmpty(swapTabData.videoTemplates)
            SwapTabsEnum.CLOTHES -> !swapTabData.isClothesTemplateLoading && CollectionUtils.isEmpty(swapTabData.clothesTemplates)
        }
    }

    fun getCurrentTabData(): List<Template> {

        return when(swapTabData.currentSelectTab) {

            SwapTabsEnum.PHOTO -> swapTabData.imageTemplates
            SwapTabsEnum.VIDEO -> swapTabData.videoTemplates
            SwapTabsEnum.CLOTHES -> swapTabData.clothesTemplates
        }
    }

    fun switchTab(tabsEnum: SwapTabsEnum) {

        swapTabData.currentSelectTab = tabsEnum

        when(tabsEnum) {
            SwapTabsEnum.PHOTO -> loadImageTemplates()
            SwapTabsEnum.VIDEO -> loadVideoTemplates()
            SwapTabsEnum.CLOTHES -> loadClothesTemplates()
        }
    }

    /**
     * 换脸模版获取
     */
    fun loadImageTemplates() {

        if (!LoginManager.isGuestLogin()) {
            return
        }

        if (swapTabData.isImageTemplateLoading || swapTabData.imageTemplates.isNotEmpty()) {
            return
        }
        swapTabData.isImageTemplateLoading = true

        val userId = LoginManager.getUserId()

        val api = Network().createApi(ApiService::class.java)
        api.getImageTemplates(
            userId = userId!!
        ).enqueue(object : Callback<CommonRespModel<List<GetImageTemplateRespDataModel>>> {
            override fun onResponse(
                call: Call<CommonRespModel<List<GetImageTemplateRespDataModel>>>,
                response: Response<CommonRespModel<List<GetImageTemplateRespDataModel>>>
            ) {
                swapTabData.isImageTemplateLoading = false

                val resp = response.body()
                if (response.isSuccessful && resp != null && resp.code == 0) {
                    swapTabData.imageTemplates = resp.data?.value ?: emptyList()
                } else {
                    //失败逻辑
                    ToastUtil.show(resp?.msg ?: "get image template fail")
                }
            }

            override fun onFailure(
                call: Call<CommonRespModel<List<GetImageTemplateRespDataModel>>>,
                t: Throwable
            ) {
                swapTabData.isImageTemplateLoading = false
                //失败逻辑
                ToastUtil.show("get image template error")
            }
        })
    }

    /**
     * 视频换脸模版获取
     */
    fun loadVideoTemplates() {

        if (!LoginManager.isGuestLogin()) {
            return
        }

        if (swapTabData.isVideoTemplateLoading || swapTabData.videoTemplates.isNotEmpty()) {
            return
        }
        swapTabData.isVideoTemplateLoading = true

        val userId = LoginManager.getUserId()

        val api = Network().createApi(ApiService::class.java)
        api.getFaceSwapVideoTemplates(
            userId = userId!!,
            app = "android",
            ch = 1,
        ).enqueue(object : Callback<CommonRespModel<List<FaceSwapVideoTemplateRespDataModel>>> {
            override fun onResponse(
                call: Call<CommonRespModel<List<FaceSwapVideoTemplateRespDataModel>>>,
                response: Response<CommonRespModel<List<FaceSwapVideoTemplateRespDataModel>>>
            ) {
                swapTabData.isVideoTemplateLoading = false

                val resp = response.body()
                if (response.isSuccessful && resp != null && resp.code == 0) {
                    swapTabData.videoTemplates = resp.data?.value ?: emptyList()
                } else {
                    //失败逻辑
                    ToastUtil.show(resp?.msg ?: "get video template fail")
                }
            }

            override fun onFailure(
                call: Call<CommonRespModel<List<FaceSwapVideoTemplateRespDataModel>>>,
                t: Throwable
            ) {
                swapTabData.isVideoTemplateLoading = false
                //失败逻辑
                ToastUtil.show("get video template error")
            }
        })
    }

    /**
     * 换衣模版获取
     */
    fun loadClothesTemplates() {

        if (!LoginManager.isGuestLogin()) {
            return
        }

        if (swapTabData.isClothesTemplateLoading || swapTabData.clothesTemplates.isNotEmpty()) {
            return
        }
        swapTabData.isClothesTemplateLoading = true

        val userId = LoginManager.getUserId()

        val api = Network().createApi(ApiService::class.java)
        api.getClothesTemplates(
            userId = userId!!
        ).enqueue(object : Callback<CommonRespModel<List<ClothesTemplateRespDataModel>>> {
            override fun onResponse(
                call: Call<CommonRespModel<List<ClothesTemplateRespDataModel>>>,
                response: Response<CommonRespModel<List<ClothesTemplateRespDataModel>>>
            ) {
                swapTabData.isClothesTemplateLoading = false

                val resp = response.body()
                if (response.isSuccessful && resp != null && resp.code == 0) {
                    swapTabData.clothesTemplates = resp.data?.value ?: emptyList()
                } else {
                    //失败逻辑
                    ToastUtil.show(resp?.msg ?: "get clothes template fail")
                }
            }

            override fun onFailure(
                call: Call<CommonRespModel<List<ClothesTemplateRespDataModel>>>,
                t: Throwable
            ) {
                swapTabData.isClothesTemplateLoading = false
                //失败逻辑
                ToastUtil.show("get clothes template error")
            }
        })
    }

    /**
     * 图片生成视频pose模版获取
     */
    fun loadImg2VideoPoseTemplate() {

        if (!LoginManager.isGuestLogin()) {
            android.util.Log.d("ViewModelDebug", "User not logged in")
            return
        }

        if (danceTabData.isImg2VideoPoseTemplateLoading || danceTabData.img2VideoPoseTemplates.isNotEmpty()) {
            android.util.Log.d("ViewModelDebug", "Already loading or has data")
            return
        }
        danceTabData.isImg2VideoPoseTemplateLoading = true
        android.util.Log.d("ViewModelDebug", "Starting to load img2video pose templates")

        val userId = LoginManager.getUserId()

        val api = Network().createApi(ApiService::class.java)
        api.getImg2VideoPoseTemplates(
            userId = userId!!,
            ch = 1,
            app = "android"
        ).enqueue(object : Callback<CommonRespModel<List<GetImg2VideoPoseTemplateRespDataModel>>> {
            override fun onResponse(
                call: Call<CommonRespModel<List<GetImg2VideoPoseTemplateRespDataModel>>>,
                response: Response<CommonRespModel<List<GetImg2VideoPoseTemplateRespDataModel>>>
            ) {
                danceTabData.isImg2VideoPoseTemplateLoading = false

                val resp = response.body()
                android.util.Log.d("ViewModelDebug", "API Response: success=${response.isSuccessful}, code=${resp?.code}, dataSize=${resp?.data?.value?.size}")
                
                if (response.isSuccessful && resp != null && resp.code == 0) {
                    danceTabData.img2VideoPoseTemplates = resp.data?.value ?: emptyList()
                    android.util.Log.d("ViewModelDebug", "Successfully loaded ${danceTabData.img2VideoPoseTemplates.size} templates")
                } else {
                    //失败逻辑
                    android.util.Log.d("ViewModelDebug", "API failed: ${resp?.msg}")
                    ToastUtil.show(resp?.msg ?: "get img2video pose template fail")
                }
            }

            override fun onFailure(
                call: Call<CommonRespModel<List<GetImg2VideoPoseTemplateRespDataModel>>>,
                t: Throwable
            ) {
                danceTabData.isImg2VideoPoseTemplateLoading = false
                android.util.Log.d("ViewModelDebug", "API call failed: ${t.message}")
                //失败逻辑
                ToastUtil.show("get img2video pose template error")
            }
        })
    }


    fun getUserCommonInfo() {
        if (LoginManager.getUserId().isNullOrEmpty()) return

        if (hotTabData.isLoading || hotTabData.configList.isNotEmpty()) {
            return
        }
        hotTabData.isLoading = true

        val api = Network().createApi(ApiService::class.java)
        api.getUserCommonInfo(
            userId = LoginManager.getUserId()!!
        ).enqueue(object : Callback<CommonRespModel<UserCommonInfoRespDataModel>> {
            override fun onResponse(
                call: Call<CommonRespModel<UserCommonInfoRespDataModel>>,
                response: Response<CommonRespModel<UserCommonInfoRespDataModel>>
            ) {
                hotTabData.isLoading = false

                val resp = response.body()
                val data = resp?.data?.value
                if (response.isSuccessful && resp != null && resp.code == 0 && data != null) {

                    if (!TextUtils.isEmpty(data.extConfig)) {
                        val type = object : TypeToken<List<UserConfigDataModel>>() {}.type
                        hotTabData.configList = Gson().fromJson(data.extConfig, type)
                    }
                }
            }
            override fun onFailure(
                call: Call<CommonRespModel<UserCommonInfoRespDataModel>>,
                t: Throwable
            ) {
                // 可选：处理异常
                hotTabData.isLoading = false
            }
        })
    }

    override fun onBackPressed(): Boolean {
        return true
    }

    override fun onLoginSuccess() {
        super.onLoginSuccess()
        updateUserInfo()
    }
}