package com.stylehub.aivideo.network

import com.google.gson.Gson
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import okio.BufferedSink
import okio.ForwardingSink
import okio.buffer
import com.stylehub.aivideo.network.model.`in`.CommonReqModel
import com.stylehub.aivideo.network.model.`in`.ReqDataInterface
import java.io.IOException

/**
 *
 * Create by league at 2025/7/9
 *
 * Write some description here
 */
class ProgressCommonReqModel<T: ReqDataInterface>(
    data: T,
    private val onProgress: (bytesWritten: Long, contentLength: Long, done: Boolean) -> Unit = {_,_,_->}
) : RequestBody() {

    private val requestBody: RequestBody = Gson().toJson(CommonReqModel(data)).toRequestBody("application/json; charset=utf-8".toMediaType())
    private var savedWriteCount: Long = 0

    override fun contentType(): MediaType? {
        return requestBody.contentType()
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return requestBody.contentLength()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val countingSink = CountingSink(sink)
        val bufferedSink = countingSink.buffer()

        requestBody.writeTo(bufferedSink)

        bufferedSink.flush()
    }

    inner class CountingSink(delegate: BufferedSink) : ForwardingSink(delegate) {
        private var bytesWritten: Long = 0
        private var contentLength: Long = 0

        @Throws(IOException::class)
        override fun write(source: Buffer, byteCount: Long) {
            super.write(source, byteCount)
            if (contentLength == 0L) {
                contentLength = contentLength()
            }

            bytesWritten += byteCount
            if (bytesWritten > savedWriteCount) {
                savedWriteCount = bytesWritten
                onProgress(bytesWritten, contentLength, bytesWritten == contentLength)
            }
        }
    }
}