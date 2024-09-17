package tj.winditask.domain.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import tj.winditask.domain.model.State

@Suppress("InjectDispatcher")
interface FlowUseCase<in Input, Output> {
    /**
     * Executes the flow on Dispatchers.IO and wraps exceptions inside it into Result
     */
    operator fun invoke(param: Input): Flow<Result<Output>> =
        execute(param)
            .catch { e ->
                emit(Result.failure(e))
            }
            .flowOn(Dispatchers.IO)

    fun execute(param: Input): Flow<Result<Output>>
}