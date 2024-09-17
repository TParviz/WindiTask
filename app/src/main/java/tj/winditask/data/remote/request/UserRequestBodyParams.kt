package tj.winditask.data.remote.request

import com.google.gson.annotations.SerializedName

data class RefreshTokenRequest(
    @SerializedName("refresh_token")
    val refreshToken: String
)

data class UserRequest(
    @SerializedName("name")
    val name: String?,
    @SerializedName("username")
    val username: String,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("city")
    val city: String?,
    @SerializedName("vk")
    val vk: String?,
    @SerializedName("instagram")
    val instagram: String?,
    @SerializedName("avatar")
    val avatar: AvatarRequest?
)

data class AvatarRequest(
    @SerializedName("filename")
    val filename: String?,
    @SerializedName("base_64")
    val base64: String?
)