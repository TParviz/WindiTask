package tj.winditask.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class RefreshTokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_id")
    val userId: Int
)

data class ProfileDataDto(
    @SerializedName("profile_data")
    val profileData: UserDto
)