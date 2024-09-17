package tj.winditask.domain.repository

import tj.winditask.data.model.PhoneSuccess
import tj.winditask.data.model.Register
import tj.winditask.data.model.UserToken

interface AuthRepository {
    suspend fun sendAuthCode(phone: String): PhoneSuccess
    suspend fun checkAuthCode(phone: String, code: String): UserToken
    suspend fun register(phone: String, name: String, username: String): Register
}