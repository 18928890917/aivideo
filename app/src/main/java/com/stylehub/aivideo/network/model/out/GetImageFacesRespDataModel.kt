package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 脸部信息获取出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/get_image_faces",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4HjvZcx3bXTfl2QHNx6hMcGyBdWGq7Y7bw=",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "jklfefgaghicijkeMNOIrstnjklfstuowxys": [
 *                 {
 *                     "bcdx5671": 1, // x1
 *                     "bcdx6782": 1212, // x2
 *                     "klmgijkerstnhijdijkevwxr": "gender", // gender
 *                     "efgaklmgijke": 12, // age
 *                     "mnoirstnhijdijkebcdx": 0, // index
 *                     "cdey5671": 2, // y1
 *                     "cdey6782": 1213 // y2
 *                 }
 *             ], // faceInfos
 *             "hijdyzauvwxrefgaxyztmnoistuorstn": 1, // duration
 *             "rstnijkeijkehijdGHICvwxrijkehijdmnoixyztwxys": 100, // needCredits
 *             "zabvmnoihijdijkestuoYZAUvwxrpqrl": "videoUrl" // videoUrl
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     ...
 * }
 */
class GetImageFacesRespDataModel {
    @SerializedName("jklfefgaghicijkeMNOIrstnjklfstuowxys")
    // faceInfos
    val faceInfos: List<FaceInfo>? = null

    @SerializedName("hijdyzauvwxrefgaxyztmnoistuorstn")
    // duration
    val duration: Int? = null

    @SerializedName("rstnijkeijkehijdGHICvwxrijkehijdmnoixyztwxys")
    // needCredits
    val needCredits: Int? = null

    @SerializedName("zabvmnoihijdijkestuoYZAUvwxrpqrl")
    // videoUrl
    val videoUrl: String? = null

    class FaceInfo {
        @SerializedName("bcdx5671")
        val x1: Int? = null
        @SerializedName("bcdx6782")
        val x2: Int? = null
        @SerializedName("klmgijkerstnhijdijkevwxr")
        val gender: String? = null
        @SerializedName("efgaklmgijke")
        val age: Int? = null
        @SerializedName("mnoirstnhijdijkebcdx")
        val index: Int? = null
        @SerializedName("cdey5671")
        val y1: Int? = null
        @SerializedName("cdey6782")
        val y2: Int? = null
    }
} 