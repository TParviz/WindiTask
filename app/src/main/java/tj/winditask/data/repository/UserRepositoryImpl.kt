package tj.winditask.data.repository

import tj.winditask.data.local.LocalDataSource
import tj.winditask.data.model.Avatar
import tj.winditask.data.model.Profile
import tj.winditask.data.model.UserParam
import tj.winditask.data.model.UserToken
import tj.winditask.data.remote.UserRemoteDataSource
import tj.winditask.domain.mapper.asEntity
import tj.winditask.domain.mapper.asExternalModel
import tj.winditask.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getAndSyncCurrentUser(): Profile {
        val userEntity = localDataSource.getSavedUser()
        return if (userEntity.id != 0 && userEntity.phone.isNotBlank() && userEntity.username.isNotBlank()) {
            userEntity.asExternalModel()
        } else {
            val entity = remoteDataSource.fetchCurrentUser().asEntity()
            localDataSource.saveAllUserData(entity)
            entity.asExternalModel()
        }
    }

    override suspend fun getCurrentUser(): Profile {
        val entity = remoteDataSource.fetchCurrentUser().asEntity()
        localDataSource.saveAllUserData(entity)
        return entity.asExternalModel()
    }

    override suspend fun updateUser(param: UserParam): Avatar {
        localDataSource.saveAllUserData(param.toUserEntity())
        return remoteDataSource.updateUser(param.toUserRequest()).asExternalModel()
    }

    override suspend fun getSavedUser(): Profile {
        return localDataSource.getSavedUser().asExternalModel()
    }

    override suspend fun getTokens(): UserToken {
        return localDataSource.getSavedTokens().asExternalModel()
    }

    override suspend fun clearTokens() {
        localDataSource.clearTokens()
    }
}