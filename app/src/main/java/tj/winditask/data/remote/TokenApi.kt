package tj.winditask.data.remote

import kotlinx.coroutines.Deferred
import retrofit2.http.Body
import retrofit2.http.POST
import tj.winditask.data.remote.dtos.RefreshTokenDto
import tj.winditask.data.remote.request.RefreshTokenRequest

interface TokenApi {
    @POST("api/v1/users/refresh-token/")
    fun refreshTokenAsync(
        @Body request: RefreshTokenRequest
    ): Deferred<RefreshTokenDto>
}