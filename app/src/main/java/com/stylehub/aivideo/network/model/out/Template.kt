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

                        it.isAccessible = true
                        val fieldClz = it.type
                        val vt1 = value.javaClass
                        val vt2 = value::class.java
                        if (fieldClz == vt1 || fieldClz == vt2) {
                            it.set(this, value)
                            return@forEach
                        }

                        val valueStr = value.toString()

                        when (fieldClz) {
                            String::class.java -> {
                                it.set(this, valueStr)
                            }
                            Integer::class.java,
                            Int::class.java -> {
                                it.set(this, valueStr.toDouble().toInt())
                            }
                            java.lang.Boolean::class.java,
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