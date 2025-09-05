package com.stylehub.aivideo.network.model.out

import java.io.Serializable

/**
 *
 * Create by league at 2025/9/4
 *
 * Write some description here
 */
class CreateImageTaskTemplateModel : Template, Serializable {

    /**
     * 任务类型
     */
    var taskType: String = ""

    /**
     * 顶部标题
     */
    var title: String? = null

    /**
     * 模板预览图
     */
    var templatePreviewUrl: String = ""

    /**
     * 模板名称
     */
    var templateName: String? = null

    /**
     * 是否需要优化，优化价格需 +1 credit
     */
    var needOpt: Boolean? = false

    /**
     * 拓展字段，通用生图任务中传入
     */
    var ext: String? = null

    /**
     * 分辨率或帧率1
     */
    var size1: String? = null

    /**
     * 分辨率或帧率2
     */
    var size2: String? = null

    /**
     * 是否为fps
     */
    var isFpsSize: Boolean = false

    /**
     * 价格
     */
    var credits1: Int? = 1

    /**
     * 价格2
     */
    var credits2: Int? = null

    /**
     * 是否支持上传第二张图片
     */
    var srcImg2Support: Boolean = false

    override fun getTmpCredits(): Int {
        return credits1?:1
    }

    override fun getTmpName(): String? {
        return templateName
    }

    override fun getTmpUrl(): String? {
        TODO("Not yet implemented")
    }

    override fun getPreviewUrl(): String {
        return templatePreviewUrl
    }

}