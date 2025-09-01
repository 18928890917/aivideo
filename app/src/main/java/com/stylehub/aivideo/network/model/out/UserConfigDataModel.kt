package com.stylehub.aivideo.network.model.out

import java.io.Serializable

/**
 *
 * Create by league at 2025/7/23
 *
 * hot列表，通用生图任务的请求信息
 */
class UserConfigDataModel: Template, Serializable {

    /**
     * 生成类型：
     * 0 = 通用生图
     * 1 = 图片换脸
     * 2 = 视频换脸
     * 3 = 换衣
     * 4 = 黏土风格
     */
    var genType = 0

    /**
     * 标题
     */
    var title: String = ""

    /**
     * 说明
     */
    var description: String = ""

    /**
     * 列表显示图片
     */
    var imgUrl: String = ""

    /**
     * 进入页面后的预览图片
     */
    var previewImgUrl: String? = null

    /**
     * 任务类型
     */
    var taskType: String = ""

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
     * 模板名称
     */
    var templateName: String? = null

    /**
     * 模板url
     */
    var templateUrl: String? = null

    /**
     * 是否支持上传第二张图片
     */
    var srcImg2Support: Boolean = false

    /**
     * 头像位置
     */
    var headPos: String? = null

    /**
     * 脸部项
     */
    var faceIndex: Int = 0

    /**
     * 模板脸部图片url
     */
    var templateFaceUrl: String? = null


    override fun getTmpCredits(): Int {
        return credits1?:1
    }

    override fun getTmpName(): String? {
        return templateName
    }

    override fun getTmpUrl(): String? {
        return templateUrl
    }

    override fun getPreviewUrl(): String {
        return previewImgUrl?: templateUrl?: imgUrl
    }

    fun convertToGetImageTemplateRespDataModel() : GetImageTemplateRespDataModel {

        val result = GetImageTemplateRespDataModel()

        result.templateUrl = getTmpUrl()
        result.templateName = getTmpName()
        result.headPos = headPos
        result.faceIndex = faceIndex

        return result
    }

    fun convertToFaceSwapVideoTemplateRespDataModel(): FaceSwapVideoTemplateRespDataModel {

        val result = FaceSwapVideoTemplateRespDataModel()

        result.templateUrl = getTmpUrl()
        result.templateName = getTmpName()
        result.credits = getTmpCredits()
        result.animate = getPreviewUrl()
        result.templateFaceUrl = templateFaceUrl

        return result
    }

    fun convertToClothesTemplateRespDataModel() : ClothesTemplateRespDataModel {

        val result = ClothesTemplateRespDataModel()

        result.templateUrl = getTmpUrl()
        result.templateName = getTmpName()
        result.credits = getTmpCredits()

        return result
    }

}