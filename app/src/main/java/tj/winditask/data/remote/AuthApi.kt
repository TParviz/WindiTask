package tj.winditask.data.remote

import retrofit2.http.Body
import retrofit2.http.POST
import tj.winditask.data.remote.dtos.*
import tj.winditask.data.remote.request.PhoneRequest
import tj.winditask.data.remote.request.PhoneWithCodeRequest
import tj.winditask.data.remote.request.RegisterRequest

interface AuthApi {
    @POST("api/v1/users/send-auth-code/")
    suspend fun sendAuthCode(
        @Body request: PhoneRequest
    ): PhoneDto

    @POST("api/v1/users/check-auth-code/")
    suspend fun checkAuthCode(
        @Body request: PhoneWithCodeRequest
    ): PhoneWithCodeDto

    @POST("api/v1/users/register/")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterDto
}
