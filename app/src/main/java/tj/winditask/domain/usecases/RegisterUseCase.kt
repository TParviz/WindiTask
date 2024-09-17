package tj.winditask.domain.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.winditask.data.model.Register
import tj.winditask.domain.repository.AuthRepository
import javax.inject.Inject

data class RegisterParam(var username: String = "", var name: String = "", var phone: String = "")

interface RegisterUseCase : FlowUseCase<RegisterParam, Register>

class RegisterUseCaseImpl @Inject constructor(
    private val repository: AuthRepository
) : RegisterUseCase {

    override fun execute(param: RegisterParam): Flow<Result<Register>> = flow {
        emit(
            Result.success(
                repository.register(
                    phone = param.phone,
                    name = param.name,
                    username = param.username
                )
            )
        )
    }

}