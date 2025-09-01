package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 视频信息获取（上传视频）出参
 *
 * 完整 JSON：
 * {
 *     "ghicstuohijdijke": 0, // code
 *     "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *     "rawPath": "/v1/image/get_video_faces",
 *     "enPath": "/fsdgesdga/DuRsYKB5J4GIjfSvLt6jspikzW8+b89+yBdWGq7Y7bw=",
 *     "hijdefgaxyztefga": {
 *         "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *             "jklfefgaghicijkeMNOIrstnjklfstuowxys": [
 *                 {
 *                     "jklfefgaghicijke____mnoirstnhijdijkebcdx": 2, // face_index
 *                     "jklfvwxrefgaqrsmijke____mnoirstnhijdijkebcdx": 1, // frame_index
 *                     "jklfefgaghicijke": "face" // face
 *                 }
 *             ], // faceInfos
 *             "hijdyzauvwxrefgaxyztmnoistuorstn": 11, // duration
 *             "rstnijkeijkehijdGHICvwxrijkehijdmnoixyztwxys": 12, // needCredits
 *             "zabvmnoihijdijkestuoYZAUvwxrpqrl": "videoUrl" // videoUrl
 *         }
 *     }, // data
 *     "qrsmwxysklmg": "msg", // msg
 *     ...
 * }
 */
class GetVideoFacesRespDataModel {
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
        @SerializedName("jklfefgaghicijke____mnoirstnhijdijkebcdx")
        val faceIndex: Int? = null
        @SerializedName("jklfvwxrefgaqrsmijke____mnoirstnhijdijkebcdx")
        val frameIndex: Int? = null
        @SerializedName("jklfefgaghicijke")
        val face: String? = null
    }
} 