package com.stylehub.aivideo.base

/**
 *
 * Create by league at 2025/7/1
 *
 * Write some description here
 */
import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel<T>(initialValue: T) : ViewModel(),
    DefaultLifecycleObserver {

    // 可以用来保存恢复状态
    private val _lifecycleState = MutableStateFlow("Not initialized")
    val lifecycleState = _lifecycleState.asStateFlow()

    protected var mActivity: Activity? = null
    protected val mData: T = initialValue
    protected val _uiStateData = MutableStateFlow(mData)
    val uiStateData: StateFlow<T> = _uiStateData

    var networkOffline by mutableStateOf(false)

    /**
     * 是否显示登录弹框
     */
    var showLoginDialog by mutableStateOf(false)

    /**
     * 是否显示loading弹框
     */
    var showLoadingDialog by mutableStateOf(false)

    /**
     * loading弹框文字
     */
    var loadingDialogTextHint by mutableStateOf("")

    /**
     * 是否显示信息提示弹窗
     */
    var showMessageHintDialog by mutableStateOf<Boolean>(false)

    /**
     * 提示文案
     */
    var messageHintDialogHint by mutableStateOf("")

    /**
     * 提示文案
     */
    var messageHintDialogTitle by mutableStateOf("")

    // 当绑定到 LifecycleOwner 时调用
    fun attach(lifecycleOwner: LifecycleOwner) {
        if (lifecycleOwner is Activity) {
            mActivity = lifecycleOwner
        }
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun detach(lifecycleOwner: LifecycleOwner) {
        mActivity = null
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    fun showNetworkOfflineDialog() {
        networkOffline = true
    }

    fun hideNetworkOfflineDialog() {
        networkOffline = false
    }

    fun showLoginDialog() {
        showLoginDialog = true
    }

    fun dismissLoginDialog() {
        showLoginDialog = false
    }

    fun showLoadingDialog(text: String = "") {
        showLoadingDialog = true
        loadingDialogTextHint = text
    }

    fun dismissLoadingDialog() {
        showLoadingDialog = false
    }


    /**
     * 显示信息提示弹窗
     */
    fun showMessageHintDialog(hint: String, title: String = messageHintDialogTitle) {
        showMessageHintDialog = true
        messageHintDialogHint = hint
        messageHintDialogTitle = title
    }

    fun dismissMessageHintDialog() {
        showMessageHintDialog = false
        messageHintDialogHint = ""
        messageHintDialogTitle = ""
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        viewModelScope.launch {
            _lifecycleState.emit("onCreate")
            onViewModelCreate()
        }
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        viewModelScope.launch {
            _lifecycleState.emit("onStart")
            onViewModelStart()
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        viewModelScope.launch {
            _lifecycleState.emit("onResume")
            onViewModelResume()
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        viewModelScope.launch {
            _lifecycleState.emit("onPause")
            onViewModelPause()
        }
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        viewModelScope.launch {
            _lifecycleState.emit("onStop")
            onViewModelStop()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
        viewModelScope.launch {
            _lifecycleState.emit("onDestroy")
            onViewModelDestroy()
        }
        super.onDestroy(owner)
    }

    // 子类可重写这些方法来响应生命周期
    open fun onViewModelCreate() {}
    open fun onViewModelStart() {}
    open fun onViewModelResume() {}
    open fun onViewModelPause() {}
    open fun onViewModelStop() {}
    open fun onViewModelDestroy() {}

    open fun onBackPressed(): Boolean{return false}

    open fun onLoginSuccess(){}

}