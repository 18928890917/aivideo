package com.stylehub.aivideo.network.model.out

import com.google.gson.annotations.SerializedName

/**
 * 获取用户通用信息 出参
 * 完整JSON示例：
 * {
 *   "ghicstuohijdijke": 0, // code
 *   "xyztvwxryzauijkeHIJDijketuvpxyztlmnh": 1, // 固定传参
 *   "rawPath": "/v1/user/common_info",
 *   "enPath": "/fsdgesdga/ZOosuuxZLKQg2ygOk79Y9I01UqwUqGQx",
 *   "hijdefgaxyztefga": {
 *     "xyztvwxryzauijkeHIJDefgaxyztefga5671": {
 *       "yzauwxysijkevwxrMNOIhijd": "userId", // userId
 *       "ghicvwxrijkehijdmnoixyzt": 5, // credit
 *       "yzauwxysijkevwxrXYZTstuoopqkijkerstn": "userToken", // userToken
 *       "yzauwxysmnoiklmgrstn": "usign", // usign
 *       "wxysyzaufghbWXYSghicvwxrmnoifghbijkeZABVefgapqrlmnoihijdXYZTmnoiqrsmijke": 0, // subScribeValidTime
 *       "ijkebcdxxyztGHICstuorstnjklfmnoiklmg": "extConfig", // extConfig
 *       "yzauwxysijkevwxrRSTNefgaqrsmijke": "userName", // userName
 *       "wxysxyztefgaxyztyzauwxys": 1, // status
 *       "tuvpefgacdeyGHICijkerstnxyztijkevwxrYZAUvwxrpqrl": "payCenterUrl", // payCenterUrl
 *       "xyzt6782MNOIGHICstuorstnjklfmnoiklmg": {
 *         "xyztijkeqrsmtuvppqrlefgaxyztijkewxys": [
 *           {
 *             "tuvpvwxrstuoqrsmtuvpxyzt": "prompt", // prompt
 *             "mnoiqrsmklmg": "img" // img
 *           }
 *         ], // templates
 *         "ghicvwxrijkehijdmnoixyztwxys": {
 *           "6782": 2, // 2
 *           "5671": 1, // 1
 *           "8904": 3 // 4
 *         } // credits
 *       }, // t2IConfig
 *       "efgazabvefgaxyztefgavwxr": "avatar", // avatar
 *       "efgatuvptuvpJKLFfghbGHICstuorstnjklfmnoiklmg": {
 *         "jklffghbGHICpqrlmnoiijkerstnxyztXYZTstuoopqkijkerstn": "fbClientToken", // fbClientToken
 *         "jklffghbGHICpqrlmnoiijkerstnxyztMNOIhijd": "fbClientId" // fbClientId
 *       }, // appFbConfig
 *       "xyztefgaklmgwxys": [
 *         "tag"
 *       ], // tags
 *       "ijkeqrsmefgamnoipqrl": "email", // email
 *       "ghicstuoyzaurstnxyztvwxrcdeyGHICstuohijdijke": "countryCode", // countryCode
 *       "jklfstuovwxrghicijkeTUVPefgacdeyGHICijkerstnxyztijkevwxr": true, // forcePayCenter
 *       "xyztcdeytuvpijke": "type", // type
 *       "zabvmnoituvp": false // vip
 *     }
 *   }, // data
 *   "qrsmwxysklmg": "msg", // msg
 *   ...
 * }
 */
data class UserCommonInfoRespDataModel(
    @SerializedName("yzauwxysijkevwxrMNOIhijd")
    val userId: String?,
    @SerializedName("ghicvwxrijkehijdmnoixyzt")
    val credit: Int?,
    @SerializedName("yzauwxysijkevwxrXYZTstuoopqkijkerstn")
    val userToken: String?,
    @SerializedName("yzauwxysmnoiklmgrstn")
    val usign: String?,
    @SerializedName("wxysyzaufghbWXYSghicvwxrmnoifghbijkeZABVefgapqrlmnoihijdXYZTmnoiqrsmijke")
    val subScribeValidTime: Long?,
    @SerializedName("ijkebcdxxyztGHICstuorstnjklfmnoiklmg")
    val extConfig: String?,
    @SerializedName("yzauwxysijkevwxrRSTNefgaqrsmijke")
    val userName: String?,
    @SerializedName("wxysxyztefgaxyztyzauwxys")
    val status: Int?,
    @SerializedName("tuvpefgacdeyGHICijkerstnxyztijkevwxrYZAUvwxrpqrl")
    val payCenterUrl: String?,
    @SerializedName("xyzt6782MNOIGHICstuorstnjklfmnoiklmg")
    val t2IConfig: T2IConfig?,
    @SerializedName("efgazabvefgaxyztefgavwxr")
    val avatar: String?,
    @SerializedName("efgatuvptuvpJKLFfghbGHICstuorstnjklfmnoiklmg")
    val appFbConfig: AppFbConfig?,
    @SerializedName("xyztefgaklmgwxys")
    val tags: List<String>?,
    @SerializedName("ijkeqrsmefgamnoipqrl")
    val email: String?,
    @SerializedName("ghicstuoyzaurstnxyztvwxrcdeyGHICstuohijdijke")
    val countryCode: String?,
    @SerializedName("jklfstuovwxrghicijkeTUVPefgacdeyGHICijkerstnxyztijkevwxr")
    val forcePayCenter: Boolean?,
    @SerializedName("xyztcdeytuvpijke")
    val type: String?,
    @SerializedName("zabvmnoituvp")
    val vip: Boolean?,
    @SerializedName("ghicvwxrijkehijdmnoixyztwxysVWXRijkeghicstuovwxrhijdYZAUvwxrpqrl")
    val pageUrl: String?
) {
    data class T2IConfig(
        @SerializedName("xyztijkeqrsmtuvppqrlefgaxyztijkewxys")
        val templates: List<Template>?,
        @SerializedName("ghicvwxrijkehijdmnoixyztwxys")
        val credits: Map<String, Int>?
    )
    data class Template(
        @SerializedName("tuvpvwxrstuoqrsmtuvpxyzt")
        val prompt: String?,
        @SerializedName("mnoiqrsmklmg")
        val img: String?
    )
    data class AppFbConfig(
        @SerializedName("jklffghbGHICpqrlmnoiijkerstnxyztXYZTstuoopqkijkerstn")
        val fbClientToken: String?,
        @SerializedName("jklffghbGHICpqrlmnoiijkerstnxyztMNOIhijd")
        val fbClientId: String?
    )
} 