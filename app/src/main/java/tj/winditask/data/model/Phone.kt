package tj.winditask.data.model

data class PhoneSuccess(val isSuccess: Boolean = false)

data class UserToken(
    val isUserExists: Boolean = false,
    val userId: Int = 0,
    val accessToken: String,
    val refreshToken: String
)

