package tj.winditask.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import tj.winditask.data.local.entities.TokenEntity
import tj.winditask.data.local.entities.UserAvatarEntity
import tj.winditask.data.local.entities.UserEntity
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class LocalDataSource(private val context: Context) {

    companion object {
        val PHONE_KEY = stringPreferencesKey("phone")
        val NAME_KEY = stringPreferencesKey("name")
        val USERNAME_KEY = stringPreferencesKey("username")
        val CITY_KEY = stringPreferencesKey("city")
        val BIRTHDAY_KEY = stringPreferencesKey("birthday")
        val USER_ID_KEY = intPreferencesKey("user_id")
        val AVATAR_KEY = stringPreferencesKey("avatar")
        val AVATAR_OTHER_KEY = stringPreferencesKey("avatar_other")
        val VK_KEY = stringPreferencesKey("vk")
        val INSTAGRAM_KEY = stringPreferencesKey("instagram")
        val ACCESS_TOKEN_SAVED_TIME = longPreferencesKey("token_time")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
    }

    suspend fun saveAllUserData(userEntity: UserEntity) {
        context.dataStore.edit { preferences ->
            with(userEntity) {
                preferences[PHONE_KEY] = phone
                preferences[NAME_KEY] = name ?: ""
                preferences[USERNAME_KEY] = username
                preferences[CITY_KEY] = city ?: ""
                preferences[BIRTHDAY_KEY] = birthday ?: ""
                preferences[USER_ID_KEY] = id
                preferences[AVATAR_KEY] = avatar ?: ""
                preferences[AVATAR_OTHER_KEY] = avatars?.avatar ?: ""
                preferences[VK_KEY] = vk ?: ""
                preferences[INSTAGRAM_KEY] = instagram ?: ""
            }
        }
    }

    suspend fun saveTokenData(
        refreshToken: String,
        accessToken: String,
        userId: Int,
        time: Long
    ) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = refreshToken
            preferences[ACCESS_TOKEN_KEY] = accessToken
            preferences[ACCESS_TOKEN_SAVED_TIME] = time
            preferences[USER_ID_KEY] = userId
        }
    }

    suspend fun getSavedUser(): UserEntity {
        val preferences = context.dataStore.data.first()
        val phone = preferences[PHONE_KEY] ?: ""
        val name = preferences[NAME_KEY]
        val username = preferences[USERNAME_KEY] ?: ""
        val city = preferences[CITY_KEY]
        val birthday = preferences[BIRTHDAY_KEY]
        val userId = preferences[USER_ID_KEY] ?: 0
        val avatar = preferences[AVATAR_KEY]
        val avatarOther = preferences[AVATAR_OTHER_KEY]
        val vk = preferences[VK_KEY]
        val instagram = preferences[INSTAGRAM_KEY]
        return UserEntity(
            name = name,
            phone = phone,
            username = username,
            city = city,
            birthday = birthday,
            id = userId,
            avatar = avatar,
            vk = vk,
            instagram = instagram,
            avatars = UserAvatarEntity(avatar = avatarOther)
        )
    }

    suspend fun clearTokens() {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN_KEY] = ""
            preferences[ACCESS_TOKEN_KEY] = ""
            preferences[USER_ID_KEY] = 0
            preferences[ACCESS_TOKEN_SAVED_TIME] = 0
        }
    }

    suspend fun getSavedTokens(): TokenEntity {
        val prefs = context.dataStore.data.first()
        val refreshToken = prefs[REFRESH_TOKEN_KEY] ?: ""
        val accessToken = prefs[ACCESS_TOKEN_KEY] ?: ""
        val userId = prefs[USER_ID_KEY] ?: 0
        val tokenTime = prefs[ACCESS_TOKEN_SAVED_TIME]
        return TokenEntity(
            refreshToken = refreshToken,
            accessToken = accessToken,
            userId = userId,
            tokenTime = tokenTime
        )
    }

    val userDataFlow: Flow<UserEntity> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // Handle IO exceptions
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val phone = preferences[PHONE_KEY] ?: ""
            val name = preferences[NAME_KEY] ?: ""
            val username = preferences[USERNAME_KEY] ?: ""
            val city = preferences[CITY_KEY] ?: ""
            val birthday = preferences[BIRTHDAY_KEY] ?: ""
            val userId = preferences[USER_ID_KEY] ?: 0
            val avatar = preferences[AVATAR_KEY] ?: ""
            val vk = preferences[VK_KEY] ?: ""
            val instagram = preferences[INSTAGRAM_KEY] ?: ""
            UserEntity(
                name = name,
                phone = phone,
                username = username,
                city = city,
                birthday = birthday,
                id = userId,
                avatar = avatar,
                vk = vk,
                instagram = instagram
            )
        }

    val tokenDataFlow: Flow<TokenEntity> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                // Handle IO exceptions
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { prefs ->
            val refreshToken = prefs[REFRESH_TOKEN_KEY] ?: ""
            val accessToken = prefs[ACCESS_TOKEN_KEY] ?: ""
            val userId = prefs[USER_ID_KEY] ?: 0
            TokenEntity(
                refreshToken = refreshToken,
                accessToken = accessToken,
                userId = userId,
            )
        }
}
