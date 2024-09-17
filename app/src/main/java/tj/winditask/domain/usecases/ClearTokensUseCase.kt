package tj.winditask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.winditask.domain.repository.UserRepository
import javax.inject.Inject

interface ClearTokensUseCase : FlowUseCase<Unit, Unit>

class ClearTokensUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : ClearTokensUseCase {

    override fun execute(param: Unit): Flow<Result<Unit>> = flow {
        emit(Result.success(repository.clearTokens()))
    }
}