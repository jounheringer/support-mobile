package com.reringuy.support.presentation.login

import com.reringuy.support.helper.OperationHandler
import com.reringuy.support.helper.Reducer
import com.reringuy.support.models.entities.User

class LoginReducer :
    Reducer<LoginReducer.LoginState, LoginReducer.LoginEvents, LoginReducer.LoginEffects> {
    sealed class LoginEvents : Reducer.ViewEvent {
        data class OnLoading(val loading: Boolean) : LoginEvents()
        data class LoadUser(val user: OperationHandler<User>) : LoginEvents()
    }

    sealed class LoginEffects : Reducer.ViewEffect {
        data class LoginError(val message: String) : LoginEffects()
        data object BiometricAuthenticated : LoginEffects()
    }

    data class LoginState(
        val loading: Boolean,
        val email: String,
        val password: String,
        val currentUser: OperationHandler<User>,
    ) : Reducer.ViewState {
        companion object {
            fun initial() = LoginState(
                loading = false,
                email = "",
                password = "",
                currentUser = OperationHandler.Waiting
            )
        }
    }

    override fun reduce(
        previousState: LoginState,
        event: LoginEvents,
    ): Pair<LoginState, LoginEffects?> = when (event) {
        is LoginEvents.LoadUser -> previousState.copy(currentUser = event.user) to null
        is LoginEvents.OnLoading -> previousState.copy(loading = event.loading) to null
    }
}