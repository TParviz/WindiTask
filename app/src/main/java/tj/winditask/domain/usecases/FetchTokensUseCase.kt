package tj.winditask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.winditask.data.model.UserToken
import tj.winditask.domain.repository.UserRepository
import javax.inject.Inject

interface FetchTokensUseCase : FlowUseCase<Unit, UserToken>

class FetchTokensUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : FetchTokensUseCase {

    override fun execute(param: Unit): Flow<Result<UserToken>> = flow {
        emit(Result.success(repository.getTokens()))
    }
}