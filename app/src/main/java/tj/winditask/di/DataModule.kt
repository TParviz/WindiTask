package tj.winditask.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import tj.winditask.data.local.LocalDataSource
import tj.winditask.data.remote.AuthApi
import tj.winditask.data.remote.AuthRemoteDataSource
import tj.winditask.data.remote.UserApi
import tj.winditask.data.remote.UserRemoteDataSource
import tj.winditask.data.repository.AuthRepositoryImpl
import tj.winditask.data.repository.UserRepositoryImpl
import tj.winditask.domain.repository.AuthRepository
import tj.winditask.domain.repository.UserRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAuthRemoteDataSource(
        api: AuthApi,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) = AuthRemoteDataSource(api, coroutineDispatcher)

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(
        api: UserApi,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ) = UserRemoteDataSource(api, coroutineDispatcher)

    @Singleton
    @Provides
    fun provideLocalDataSource(
        @ApplicationContext appContext: Context,
    ) = LocalDataSource(appContext)

    @Singleton
    @Provides
    fun provideAuthRepository(
        localDataSource: LocalDataSource,
        authRemoteDataSource: AuthRemoteDataSource
    ): AuthRepository = AuthRepositoryImpl(localDataSource, authRemoteDataSource)

    @Singleton
    @Provides
    fun provideUserRepository(
        localDataSource: LocalDataSource,
        userRemoteDataSource: UserRemoteDataSource
    ): UserRepository = UserRepositoryImpl(localDataSource, userRemoteDataSource)

}