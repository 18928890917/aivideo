package com.stylehub.aivideo.utils

import java.lang.reflect.Modifier

/**
 *
 * Create by league at 2025/7/2
 *
 * 通过反射获取对象的所有字段及其值（包括私有字段）
 */

/**
 * 反射获取对象字段信息，支持嵌套结构、集合、数组等
 */
fun Any?.toDeepDebugFieldMap(): Any? {
    if (this == null) return null

    return when (this) {
        is Map<*, *> -> this.mapKeys { it.key.toString() }
        is Collection<*> -> this.map { it?.toDeepDebugFieldMap() }
        is Array<*> -> this.map { it?.toDeepDebugFieldMap() }

        // 单独处理原始类型数组
        is BooleanArray -> this.toList()
        is ByteArray -> this.toList()
        is CharArray -> this.toList()
        is ShortArray -> this.toList()
        is IntArray -> this.toList()
        is LongArray -> this.toList()
        is FloatArray -> this.toList()
        is DoubleArray -> this.toList()

        else -> {
            if (isSimpleType(this)) {
                this
            } else {
                val result = mutableMapOf<String, Any?>()
                var clazz: Class<out Any>? = this::class.java

                while (clazz != null) {
                    for (field in clazz.declaredFields) {
                        if (Modifier.isStatic(field.modifiers)) continue
                        if (field.name == "\$stable") continue
                        if (field.name == "serialVersionUID") continue

                        field.isAccessible = true
                        try {
                            val value = field.get(this)
                            result[field.name] = value.toDeepDebugFieldMap()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    clazz = clazz.superclass
                }
                result
            }
        }
    }
}

private val simpleTypes = listOf(
    String::class.java,
    Number::class.java,
    Boolean::class.java,
    Char::class.java
)

private fun isSimpleType(value: Any): Boolean {
    val type = value.javaClass

    return when {
        type.isPrimitive -> true
        value is Enum<*> -> true
        simpleTypes.any { it.isAssignableFrom(type) } -> true
        else -> false
    }
}
