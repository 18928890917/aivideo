package com.stylehub.aivideo.constants

/**
 *
 * Create by league at 2025/7/6
 *
 * Write some description here
 */

enum class GetImageProgressStateEnum(val code: Int, val desc: String) {

    InTheQueue(1, "队列中"),

    Making(2, "进行中"),

    Finished(3, "任务完成"),

    Timeout(4, "超时"),

    Error(5, "Error")
}

enum class MyTaskTypeEnum(val code: Int, val desc: String) {

    ImageFaceSwap(2, "图片换脸"),

    ClothesSwap(3, "换衣"),

    VideoFaceSwap(5, "视频换脸"),

    ClayStyle(7, "黏土风格"),

    DanceVideo(8, "跳舞视频"),

    AdvanceFaceSwap(11, "高级换脸"),

    FpDance(13, "舞蹈"),

    CombinePicTask(14, "图片合并"),

    I2VCustomTask(15, "用户自定义"),

    DollGenTask(16, "人物手办图"),

    CartoonStyleTask(17, "卡通风格图"),

    ClayAndCartoonStyleTask(19, "黏土手办任务")
}

enum class MyTaskImgTypeEnum(val code: Int, val desc: String) {

    Image(1, "图片"),

    Video(2, "视频")
}