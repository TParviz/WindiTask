package tj.winditask.data.remote

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import tj.winditask.data.local.LocalDataSource
import tj.winditask.data.local.LocalDataSource.Companion.ACCESS_TOKEN_KEY
import tj.winditask.data.local.LocalDataSource.Companion.ACCESS_TOKEN_SAVED_TIME
import tj.winditask.data.local.LocalDataSource.Companion.REFRESH_TOKEN_KEY
import tj.winditask.data.local.dataStore
import tj.winditask.data.remote.request.RefreshTokenRequest

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")

        val token = runBlocking {
            context.dataStore.data.map { prefs ->
                prefs[ACCESS_TOKEN_KEY]
            }.first()
        }
        val tokenTime = runBlocking {
            context.dataStore.data.map { prefs ->
                prefs[ACCESS_TOKEN_SAVED_TIME]
            }.first()
        }

        if (!token.isNullOrBlank() && tokenTime != null && System.currentTimeMillis() >= tokenTime) {
            val tokenApi = ServiceGenerator.generate(TokenApi::class.java)
            runBlocking {
                val refreshToken = context.dataStore.data.map { prefs ->
                    prefs[REFRESH_TOKEN_KEY]
                }.first() ?: ""
                val refreshTokenDto =
                    tokenApi.refreshTokenAsync(RefreshTokenRequest(refreshToken)).await()
                context.dataStore.edit { preferences ->
                    preferences[REFRESH_TOKEN_KEY] = refreshTokenDto.refreshToken
                    preferences[ACCESS_TOKEN_KEY] = refreshTokenDto.accessToken
                    preferences[LocalDataSource.USER_ID_KEY] = refreshTokenDto.userId
                    preferences[ACCESS_TOKEN_SAVED_TIME] =
                        System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
                }
                request.addHeader("Authorization", "Bearer ${refreshTokenDto.refreshToken}")
            }
        } else {
            token?.let {
                request.addHeader("Authorization", "Bearer $token")
            }
        }

        return chain.proceed(request.build())
    }
}