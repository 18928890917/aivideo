package com.stylehub.aivideo.network.model.out

import java.io.Serializable

/**
 *
 * Create by league at 2025/7/23
 *
 * hot列表，通用生图任务的请求信息
 */
class UserConfigDataModel: Serializable {

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
     * 模板信息
     * 注：
     * 当存在生图类型列表的时候，如果不传模板则会跳转到对应的功能模块中
     * 例如：
     * 生图类型为：图片换脸 genType = 1
     * 传了模板则直接跳转到图片换脸功能
     * 没传则跳转到图片换脸模块中去选择模板
     */
    var template: HashMap<String, Any>? = null

    fun <T : Template> getTemplateModel(): T? {

        if (template == null)
            return null

        when(genType) {

            1 -> {
                //图片换脸
                val result = GetImageTemplateRespDataModel()
                result.setFromMap(template)
                return result as T
            }
            2 -> {
                //视频换脸
                val result = FaceSwapVideoTemplateRespDataModel()
                result.setFromMap(template)
                return result as T

            }
            3 -> {
                //换衣
                val result = ClothesTemplateRespDataModel()
                result.setFromMap(template)
                return result as T
            }
            4 -> {
                return null
            }
            else -> {
                //通用生图模板
                val result = CreateImageTaskTemplateModel()
                result.setFromMap(template)
                return result as T
            }

        }
    }
}