package com.stylehub.aivideo.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/*
 * SharedPreferenceUtil
 *
 * 用于简化 SharedPreferences 的数据存储与读取，支持基础类型和对象类型（自动序列化/反序列化）。
 *
 * 使用方法：
 * // 1. 在 Application 或首个 Activity 初始化
 * SharedPreferenceUtil.init(context)
 *
 * // 2. 存储数据（支持基础类型和对象）
 * SharedPreferenceUtil.put("key", value)
 *
 * // 3. 获取数据（可设置默认值，支持对象还原）
 * val str: String? = SharedPreferenceUtil.get("key", "default")
 * val user: User? = SharedPreferenceUtil.get("userKey")
 *
 * // 4. 移除/清空
 * SharedPreferenceUtil.remove("key")
 * SharedPreferenceUtil.clear()
 */

object SharedPreferenceUtil {
    private const val PREF_NAME = "aiswap_pref"
    @PublishedApi
    internal lateinit var prefs: SharedPreferences
    @PublishedApi
    internal val gson = Gson()

    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun put(key: String, value: Any?) {
        with(prefs.edit()) {
            when (value) {
                is String? -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                null -> remove(key)
                else -> putString(key, gson.toJson(value)) // 对象序列化为json
            }
            apply()
        }
    }

    inline fun <reified T> get(key: String, default: T? = null): T? {
        return when (T::class) {
            String::class -> prefs.getString(key, default as? String) as T?
            Int::class -> prefs.getInt(key, default as? Int ?: -1) as T?
            Boolean::class -> prefs.getBoolean(key, default as? Boolean ?: false) as T?
            Float::class -> prefs.getFloat(key, default as? Float ?: -1f) as T?
            Long::class -> prefs.getLong(key, default as? Long ?: -1L) as T?
            else -> {
                val json = prefs.getString(key, null)
                if (json != null) gson.fromJson<T>(json, object : TypeToken<T>() {}.type) else default
            }
        }
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }
} 