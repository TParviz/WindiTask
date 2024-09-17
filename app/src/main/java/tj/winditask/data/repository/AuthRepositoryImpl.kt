package tj.winditask.data.repository

import tj.winditask.data.local.LocalDataSource
import tj.winditask.data.model.PhoneSuccess
import tj.winditask.data.model.Register
import tj.winditask.data.model.UserToken
import tj.winditask.data.remote.AuthRemoteDataSource
import tj.winditask.domain.mapper.asEntity
import tj.winditask.domain.mapper.asExternalModel
import tj.winditask.domain.mapper.asExternalModel2
import tj.winditask.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun sendAuthCode(phone: String): PhoneSuccess {
        return remoteDataSource.sendAuthCode(phone).asExternalModel()
    }

    override suspend fun checkAuthCode(phone: String, code: String): UserToken {
        val entity = remoteDataSource.checkAuthCode(phone, code).asEntity()
        if (entity.isUserExists) {
            localDataSource.saveTokenData(
                refreshToken = entity.refreshToken ?: "",
                accessToken = entity.accessToken ?: "",
                userId = entity.userId,
                time = System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
            )
        }
        return entity.asExternalModel()
    }

    override suspend fun register(phone: String, name: String, username: String): Register {
        val entity =
            remoteDataSource.register(name = name, username = username, phone = phone).asEntity()

        localDataSource.saveTokenData(
            refreshToken = entity.refreshToken ?: "",
            accessToken = entity.accessToken ?: "",
            userId = entity.userId,
            time = System.currentTimeMillis() + 10 * 60 * 1000 //10 min in millis
        )

        return entity.asExternalModel2()
    }
}