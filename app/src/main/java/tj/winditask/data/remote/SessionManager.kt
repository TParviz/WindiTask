package tj.winditask.data.remote


class SessionManager {
    // Other session management properties and methods

    private var accessToken: String? = null
    private var accessTokenExpirationTime: Long = 10 * 60 * 1000 // 10 minutes in milliseconds

    // Method to check if the access token has expired
    fun isAccessTokenExpired(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        return currentTimeMillis >= accessTokenExpirationTime
    }

    // Method to update the access token and its expiration time in the session
    fun updateAccessToken(token: String, expiresIn: Long) {
        accessToken = token
        accessTokenExpirationTime += System.currentTimeMillis() // Convert expiresIn to milliseconds
    }

}