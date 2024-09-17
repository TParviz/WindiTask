package tj.winditask.domain.model

sealed class State<out R> {
    data class Success<out T>(
        val data: T?
    ) : State<T>()

    data class Error(
        val error: Throwable? = null,
        val message: String? = null
    ) : State<Nothing>()

    object Loading : State<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[message=$message]"
            Loading  -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val State<*>.succeeded
    get() = this is State.Success && data != null