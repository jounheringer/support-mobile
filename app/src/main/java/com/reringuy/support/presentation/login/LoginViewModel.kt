package com.reringuy.support.presentation.login

import androidx.lifecycle.viewModelScope
import com.reringuy.support.auth.TokenManager
import com.reringuy.support.helper.BaseViewModel
import com.reringuy.support.helper.OperationHandler
import com.reringuy.support.models.data.EmailPassword
import com.reringuy.support.presentation.login.LoginReducer.LoginEvents
import com.reringuy.support.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val tokenManager: TokenManager,
    private val authRepository: AuthRepository,
) : BaseViewModel<LoginReducer.LoginState, LoginEvents, LoginReducer.LoginEffects>(
    initialState = LoginReducer.LoginState.initial(),
    reducer = LoginReducer()
) {
    fun loadUser() {
        sendEvent(LoginEvents.OnLoading(true))
        viewModelScope.launch {
            tokenManager.getCurrentUser().collect {
                if (it == null)
                    sendEvent(LoginEvents.LoadUser(OperationHandler.Error("Usuario nao encontrato.")))
                else
                    sendEvent(LoginEvents.LoadUser(OperationHandler.Success(it)))
            }
            sendEvent(LoginEvents.OnLoading(false))
        }
    }

    fun onLogin(auth: EmailPassword) {
        sendEvent(LoginEvents.OnLoading(true))
        viewModelScope.launch {
            authRepository.login(auth).collect {
                if (it == null) {
                    sendEvent(LoginEvents.LoadUser(OperationHandler.Error("Usuario nao encontrato.")))
                } else {
                    tokenManager.saveToken(it.token)
                    tokenManager.saveUser(it.user)
                    sendEvent(LoginEvents.LoadUser(OperationHandler.Success(it.user)))
                }
            }
            sendEvent(LoginEvents.OnLoading(false))
        }
    }
}