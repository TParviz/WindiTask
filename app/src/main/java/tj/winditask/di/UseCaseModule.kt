package tj.winditask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tj.winditask.domain.repository.AuthRepository
import tj.winditask.domain.repository.UserRepository
import tj.winditask.domain.usecases.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideSendAuthCodeUseCase(
        repository: AuthRepository
    ): SendAuthCodeUseCase = SendAuthCodeUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideCheckAuthCodeUseCase(
        repository: AuthRepository
    ): CheckAuthCodeUseCase = CheckAuthCodeUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideRegisterUseCase(
        repository: AuthRepository
    ): RegisterUseCase = RegisterUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideFetchUserUseCase(
        repository: UserRepository
    ): FetchUserUseCase = FetchUserUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideFetchTokensUseCase(
        repository: UserRepository
    ): FetchTokensUseCase = FetchTokensUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideClearTokensUseCase(
        repository: UserRepository
    ): ClearTokensUseCase = ClearTokensUseCaseImpl(repository)

    @Singleton
    @Provides
    fun provideUpdateUserUseCase(
        repository: UserRepository
    ): UpdateUserUseCase = UpdateUserUseCaseImpl(repository)
}