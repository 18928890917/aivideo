package com.stylehub.aivideo.network.model.out

/**
 *
 * Create by league at 2025/7/4
 *
 * Write some description here
 */
interface Template {
    fun getTmpCredits(): Int?
    fun getTmpName(): String?
    fun getTmpUrl(): String?
    fun getPreviewUrl(): String?{return getTmpUrl()}

    fun setFromMap(map: Map<String, Any>?) {

        if (map == null)
            return

        val fields = javaClass.declaredFields
        fields.forEach {
            try {
                val name = it.name
                if (map.containsKey(name)) {
                    val value = map[name]
                    if (value != null) {

                        val valueStr = value.toString()
                        val fieldClz = it.type
                        it.isAccessible = true

                        when (fieldClz) {
                            String::class.java -> {
                                it.set(this, valueStr)
                            }
                            Int::class.java -> {
                                it.set(this, valueStr.toInt())
                            }
                            Boolean::class.java -> {
                                it.set(this, valueStr.toBooleanStrict())
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }
}