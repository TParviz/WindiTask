package tj.winditask.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import tj.winditask.data.model.*
import tj.winditask.domain.model.State
import tj.winditask.domain.usecases.*
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val fetchUserUseCase: FetchUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val fetchTokensUseCase: FetchTokensUseCase,
    private val cleanTokensUseCase: ClearTokensUseCase
) : ViewModel() {

    private val _profileState = MutableLiveData<State<Profile>>()
    val profileState: LiveData<State<Profile>> get() = _profileState

    private val _updateProfileState = MutableLiveData<State<Avatar>>()
    val updateProfileState: LiveData<State<Avatar>> get() = _updateProfileState

    private val _userTokenState = MutableLiveData<UserTokenState>()
    val userTokenState: LiveData<UserTokenState> get() = _userTokenState

    private val _clearTokenState = MutableLiveData<ClearTokenState>()
    val clearTokenState: LiveData<ClearTokenState> get() = _clearTokenState

    var userParam: UserParam? = null

    init {
        fetchUser()
    }

    fun fetchAndSyncUser() {
        viewModelScope.launch {
            fetchUserUseCase(true)
                .onStart {
                    _profileState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _profileState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _profileState.value = State.Success(it)
                    }
                }
        }
    }

    fun fetchUser() {
        viewModelScope.launch {
            fetchUserUseCase(false)
                .onStart {
                    _profileState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _profileState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _profileState.value = State.Success(it)
                    }
                }
        }
    }

    fun updateUser() {
        viewModelScope.launch {
            updateUserUseCase(checkNotNull(userParam))
                .onStart {
                    _updateProfileState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _updateProfileState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _updateProfileState.value = State.Success(it)
                    }
                }
        }
    }

    fun getUserToken() {
        viewModelScope.launch {
            fetchTokensUseCase(Unit).collectLatest {
                it.onSuccess { aUserToken ->
                    _userTokenState.value = UserTokenState(userToken = aUserToken)
                }
                it.onFailure { aThrowable ->
                    _userTokenState.value =
                        UserTokenState(errorMessage = aThrowable.localizedMessage)
                }
            }
        }
    }

    fun clearTokens() {
        viewModelScope.launch {
            cleanTokensUseCase(Unit).collectLatest {
                it.onSuccess {
                    _clearTokenState.value = ClearTokenState(isCleared = true)
                }
                it.onFailure { aThrowable ->
                    _clearTokenState.value = ClearTokenState(errorMessage = aThrowable.message)
                }
            }
        }
    }
}

data class UserTokenState(
    val userToken: UserToken? = null,
    val errorMessage: String? = null
)

data class ClearTokenState(
    val isCleared: Boolean = false,
    val errorMessage: String? = null
)
