package tj.winditask.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("name")
    val name: String?,
    @SerializedName("username")
    val username: String,
    @SerializedName("birthday")
    val birthday: String?,
    @SerializedName("avatar")
    val avatar: String? ,
    @SerializedName("city")
    val city: String?,
    @SerializedName("vk")
    val vk: String?,
    @SerializedName("instagram")
    val instagram: String?,
    @SerializedName("id")
    val id: Int,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("avatars")
    val avatars: AvatarDto?
)

