package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 图片动作风格化gif出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/offtop_video",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4GZkgR+UpnK_zW+uc2XbwwF",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "wxysxyztefgaxyztijke": 0, // state
 *             "xyztefgawxysopqkMNOIhijd": 23123 // taskId
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     "vwxrefgarstnhijd6782": "CK9uW4C8", // 固定传参
 *     "vwxrefgarstnhijd7893": "OLRyrBoi", // 固定传参
 *     "vwxrefgarstnhijd5671": "OH9BbCmt", // 固定传参
 *     "vwxrefgarstnhijd4560": "dmrwDpaf" // 固定传参
 * }
 */
data class OfftopVideoRespDataModel(
    @SerializedName("wxysxyztefgaxyztijke")
    val state: Int?,
    @SerializedName("xyztefgawxysopqkMNOIhijd")
    val taskId: Long?
)