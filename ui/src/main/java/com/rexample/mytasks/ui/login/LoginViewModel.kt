package com.rexample.mytasks.ui.login

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.repository.IUserRepository
import com.rexample.mytasks.data.repository.UserRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: IUserRepository): BaseViewModel<LoginUiState, LoginUiAction>() {

    override val _state = MutableStateFlow(LoginUiState())

    override fun doAction(action: LoginUiAction) {
        when(action) {
            is LoginUiAction.LoginUser -> loginUser()
            is LoginUiAction.UpdateEmail -> setEmailInput(action.email)
            is LoginUiAction.UpdatePassword -> setPasswordInput(action.password)
            LoginUiAction.HidePassword -> {
                _state.update { state -> state.copy(passwordHidden = true) }
            }
            LoginUiAction.ShowPassword -> {
                _state.update { state -> state.copy(passwordHidden = false) }
            }
        }
    }

    private fun setEmailInput(newEmailInput: String) {
        _state.update {
            it.copy(
                emailInput = newEmailInput,
                emailInputStatus = Resource.Success("")
            )
        }

        if (!isValidEmail(state.value.emailInput)) {
            _state.update { it.copy(emailInputStatus = Resource.Error("Email tidak valid")) }
            return
        }
    }

    private fun setPasswordInput(newPasswordInput: String) {
        _state.update {
            it.copy(
                passwordInput = newPasswordInput,
                passwordInputStatus = Resource.Success("")
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }

    private fun loginUser() {
        viewModelScope.launch {
            userRepository.login(
                state.value.emailInput,
                state.value.passwordInput
            ).collectLatest {
                _state.update { state ->
                    state.copy(
                        loginResult = it
                    )
                }
            }
        }
    }
}

data class LoginUiState(
    val passwordInputStatus: Resource<String>? = Resource.Idle(),
    val emailInputStatus: Resource<String>? = Resource.Idle(),
    val emailInput: String = "",
    val passwordInput: String = "",
    val passwordHidden: Boolean = true,
    val loginResult: Resource<UserEntity?> = Resource.Idle()
)

sealed class LoginUiAction{
    data object LoginUser: LoginUiAction()
    data class UpdateEmail(val email: String) : LoginUiAction()
    data class UpdatePassword(val password: String) : LoginUiAction()
    data object ShowPassword : LoginUiAction()
    data object HidePassword : LoginUiAction()
}