package tj.winditask.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import tj.winditask.data.remote.dtos.*
import tj.winditask.data.remote.request.UserRequest

interface UserApi {
    @GET("api/v1/users/me/")
    suspend fun getCurrentUser(): ProfileDataDto

    @PUT("api/v1/users/me/")
    suspend fun updateUser(
        @Body request: UserRequest
    ): AvatarsDto
}