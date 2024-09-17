package tj.winditask.presentation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import tj.winditask.data.model.PhoneSuccess
import tj.winditask.data.model.Register
import tj.winditask.data.model.UserToken
import tj.winditask.domain.model.State
import tj.winditask.domain.usecases.*
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val sendAuthCodeUseCase: SendAuthCodeUseCase,
    private val checkAuthCodeUseCase: CheckAuthCodeUseCase,
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _sendAuthCodeState = MutableLiveData<State<PhoneSuccess>>()
    val sendAuthCodeState: LiveData<State<PhoneSuccess>> get() = _sendAuthCodeState

    private val _checkAuthCodeState = MutableLiveData<State<UserToken>>()
    val checkAuthCodeState: LiveData<State<UserToken>> get() = _checkAuthCodeState

    private val _registerState = MutableLiveData<State<Register>>()
    val registerState: LiveData<State<Register>> get() = _registerState

    var registerParam: RegisterParam = RegisterParam()

    fun sendAuthCode(phone: String) {
        viewModelScope.launch {
            sendAuthCodeUseCase(PhoneParam(phone))
                .onStart {
                    _sendAuthCodeState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _sendAuthCodeState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _sendAuthCodeState.value = State.Success(it)
                    }
                }
        }
    }

    fun checkAuthCode(phone: String, code: String) {
        viewModelScope.launch {
            checkAuthCodeUseCase(PhoneWithCodeParam(phone = phone, code = code))
                .onStart {
                    _checkAuthCodeState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _checkAuthCodeState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _checkAuthCodeState.value = State.Success(it)
                    }
                }
        }
    }

    fun register() {
        viewModelScope.launch {
            registerUseCase(registerParam)
                .onStart {
                    _registerState.value = State.Loading
                }.collect { result ->
                    result.onFailure { error ->
                        _registerState.value = State.Error(message = error.message)
                    }.onSuccess {
                        _registerState.value = State.Success(it)
                    }
                }
        }
    }
}