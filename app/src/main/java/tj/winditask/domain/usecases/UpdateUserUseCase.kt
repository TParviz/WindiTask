package tj.winditask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.winditask.data.model.Avatar
import tj.winditask.data.model.UserParam
import tj.winditask.domain.model.State
import tj.winditask.domain.repository.UserRepository
import javax.inject.Inject

interface UpdateUserUseCase : FlowUseCase<UserParam, Avatar>

class UpdateUserUseCaseImpl @Inject constructor(
    private val repository: UserRepository
) : UpdateUserUseCase {

    override fun execute(param: UserParam): Flow<Result<Avatar>> = flow {
        emit(Result.success(repository.updateUser(param)))
    }
}