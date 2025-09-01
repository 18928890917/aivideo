package com.stylehub.aivideo.utils

import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.stylehub.aivideo.AiSwapApplication

/**
 * DownloadUtil
 *
 * 使用Android原生DownloadManager进行文件下载
 *
 * 用法示例：
 * DownloadUtil.downloadFile(context, url, fileName, description)
 */
object DownloadUtil {

    private fun getActivity(): Activity? {


        return AiSwapApplication.getInstance().getTopActivity()
    }

    /**
     * 使用系统下载器下载文件
     * @param context Context
     * @param url 下载链接
     * @param fileName 保存的文件名
     * @param description 下载描述（可选）
     * @return 下载任务ID
     */
    fun downloadFile(
        context: Context,
        url: String,
        fileName: String,
        description: String = ""
    ): Long {

        if (getActivity() == null)
            return -1
        val act = getActivity()!!

        if (!XXPermissions.isGranted(act, Permission.WRITE_EXTERNAL_STORAGE)) {

            if (XXPermissions.isDoNotAskAgainPermissions(act, Permission.WRITE_EXTERNAL_STORAGE)) {
                ToastUtil.show("Please turn on storage permission")
                XXPermissions.startPermissionActivity(act, Permission.WRITE_EXTERNAL_STORAGE)
            } else {

                XXPermissions
                    .with(act)
                    .permission(Permission.WRITE_EXTERNAL_STORAGE)
                    .request { permissions, allGranted ->
                        if (allGranted) {
                            downloadFile(context, url, fileName, description)
                        } else {
                            ToastUtil.show("No storage permission")
                        }
                    }
            }
            return -1
        }

        val request = DownloadManager.Request(Uri.parse(url))
        request.setTitle(fileName)
        if (description.isNotEmpty()) {
            request.setDescription(description)
        }
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
        request.setAllowedOverMetered(true)
        request.setAllowedOverRoaming(true)

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        return downloadManager.enqueue(request)
    }
} 