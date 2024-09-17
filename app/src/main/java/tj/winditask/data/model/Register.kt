package tj.winditask.data.model

data class Register(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int = 0
)