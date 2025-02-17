package com.rexample.mytasks.ui.register

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.repository.IUserRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(private val userRepository: IUserRepository) : BaseViewModel<RegisterUiState, RegisterUiAction>() {

    override val _state = MutableStateFlow(RegisterUiState())

    override fun doAction(action: RegisterUiAction) {
        when (action) {
            is RegisterUiAction.RegisterUser -> registerUser()
            is RegisterUiAction.UpdateEmail -> setEmailInput(action.email)
            is RegisterUiAction.UpdatePassword -> setPasswordInput((action.password))
            is RegisterUiAction.UpdateConfirmPassword -> setConfirmPasswordInput(action.confirmPassword)
            is RegisterUiAction.HidePassword -> {
                _state.update { state -> state.copy(passwordHidden = true) }
            }
            is RegisterUiAction.ShowPassword -> {
                _state.update { state -> state.copy(passwordHidden = false) }
            }
            is RegisterUiAction.HideConfirmPassword -> {
                _state.update { state -> state.copy(confirmPasswordHidden = true) }
            }
            is RegisterUiAction.ShowConfirmPassword -> {
                _state.update { state -> state.copy(confirmPasswordHidden = false) }
            }

            is RegisterUiAction.UpdateUsername -> setUsernameInput(action.username)
        }
    }

    private fun setUsernameInput(newUsernameInput: String) {
        _state.update {
            it.copy(
                usernameInput = newUsernameInput,
                usernameInputStatus = Resource.Success("")
            )
        }

        if (state.value.usernameInput.length < 3) {
            _state.update { it.copy(usernameInputStatus = Resource.Error("Nama minimal 3 karakter")) }
        }
    }

    private fun setEmailInput(newEmailInput: String) {
        _state.update {
            it.copy(
                emailInput = newEmailInput,
                emailInputStatus = Resource.Success("")
            )
        }

        if (!isValidEmail(state.value.emailInput) ) {
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

        if (state.value.passwordInput.length < 8) {
            _state.update { it.copy(passwordInputStatus = Resource.Error("Password minimal 8 karakter")) }
        }
    }

    private fun setConfirmPasswordInput(newConfirmPasswordInput: String) {
        _state.update {
            it.copy(
                confirmPasswordInput = newConfirmPasswordInput,
                confirmPasswordInputStatus = Resource.Success("")
            )
        }

        if (state.value.confirmPasswordInput != state.value.passwordInput) {
            _state.update { it.copy(confirmPasswordInputStatus = Resource.Error("Kofirmasi password salah")) }
        }
    }

    private fun registerUser() {
        viewModelScope.launch {
            val existingUser = userRepository.isEmailUsed(state.value.emailInput)
            if (existingUser.data == true) {
                _state.update { it.copy(emailInputStatus = Resource.Error("Email sudah terdaftar")) }
                return@launch
            }

            if (
                state.value.emailInputStatus is Resource.Success &&
                state.value.confirmPasswordInputStatus is Resource.Success &&
                state.value.confirmPasswordInputStatus is Resource.Success
            ) {
                val user = UserEntity(
                    email = state.value.emailInput,
                    password = state.value.passwordInput,
                    username = state.value.usernameInput
                )
                userRepository.register(user).collectLatest { result ->
                    _state.update { state ->
                        state.copy(
                            registerResult = result
                        )
                    }
                }
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".")
    }
}



data class RegisterUiState(
    val confirmPasswordInputStatus: Resource<String>? = Resource.Idle(),
    val passwordInputStatus: Resource<String>? = Resource.Idle(),
    val emailInputStatus: Resource<String>? = Resource.Idle(),
    val usernameInputStatus: Resource<String>? = Resource.Idle(),
    val registerResult: Resource<Unit> = Resource.Idle(),
    val emailInput: String = "",
    val passwordInput: String = "",
    val usernameInput: String = "",
    val confirmPasswordInput: String = "",
    val passwordHidden: Boolean = true,
    val confirmPasswordHidden: Boolean = true
)

sealed class RegisterUiAction {
    data object RegisterUser : RegisterUiAction()
    data class UpdateEmail(val email: String) : RegisterUiAction()
    data class UpdatePassword(val password: String) : RegisterUiAction()
    data class UpdateConfirmPassword(val confirmPassword: String) : RegisterUiAction()
    data class UpdateUsername(val username: String) : RegisterUiAction()
    data object ShowPassword : RegisterUiAction()
    data object HidePassword : RegisterUiAction()
    data object ShowConfirmPassword : RegisterUiAction()
    data object HideConfirmPassword : RegisterUiAction()
}
