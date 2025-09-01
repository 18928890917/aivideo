package com.stylehub.aivideo.utils

import android.annotation.SuppressLint
import java.security.MessageDigest

/**
 * EncryptUtil
 *
 * 提供常用加密工具方法
 *
 * 用法示例：
 * val md5Str = EncryptUtil.md5("hello")
 * val md5Str16 = EncryptUtil.md5("hello", length = 16, upperCase = true)
 */
object EncryptUtil {
    /**
     * 计算字符串的MD5值
     * @param src 输入字符串
     * @param length 输出位数，16 或 32，默认32
     * @param upperCase 是否大写，默认false（小写）
     * @return MD5字符串
     */
    fun md5(src: String, length: Int = 32, upperCase: Boolean = false): String {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(src.toByteArray())
        var result = bytes.joinToString("") { "%02x".format(it) }
        if (length == 16) {
            // 取中间16位
            result = result.substring(8, 24)
        }
        return if (upperCase) result.uppercase() else result
    }

    /**
     * AES加密（ECB/PKCS5Padding，key不足16补零）
     */
    @SuppressLint("GetInstance")
    fun aes(src: String, key: String, hexResult: Boolean = false): String {
        try {
            val charset = Charsets.UTF_8
            val keyBytes = key.toByteArray(charset).copyOf(16)
            val secretKey = javax.crypto.spec.SecretKeySpec(keyBytes, "AES")
            val cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey)
            val encrypted = cipher.doFinal(src.toByteArray(charset))
            var result = encrypted.joinToString("") { "%02x".format(it) }
            if (!hexResult) {
                result = hex2Base64(result)
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }

    /**
     * AES解密（ECB/PKCS5Padding，支持Base64或Hex输入）
     */
    fun aesDecrypt(base64OrHex: String, key: String, isBase64: Boolean = true): String {
        return try {
            val charset = Charsets.UTF_8
            val keyBytes = key.toByteArray(charset).copyOf(16)
            val secretKey = javax.crypto.spec.SecretKeySpec(keyBytes, "AES")
            val cipher = javax.crypto.Cipher.getInstance("AES/ECB/PKCS5Padding")
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey)
            val encryptedBytes = if (isBase64) {
                android.util.Base64.decode(base64OrHex, android.util.Base64.NO_WRAP)
            } else {
                base64OrHex.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            }
            val decrypted = cipher.doFinal(encryptedBytes)
            String(decrypted, charset)
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * 16进制字符串转Base64字符串
     */
    fun hex2Base64(hexString: String): String {
        return try {
            val bytes = hexString.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (e: Exception) {
            ""
        }
    }
} 