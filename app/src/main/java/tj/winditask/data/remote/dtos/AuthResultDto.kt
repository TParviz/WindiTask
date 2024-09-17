package tj.winditask.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class PhoneDto(
    @SerializedName("is_success")
    val isSuccess: Boolean
)

data class PhoneWithCodeDto(
    @SerializedName("is_user_exists")
    val isUserExists: Boolean,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("access_token")
    val accessToken: String?,
    @SerializedName("refresh_token")
    val refreshToken: String?
)

data class RegisterDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_id")
    val userId: Int
)