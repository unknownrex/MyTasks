package com.rexample.mytasks

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.repository.IUserRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AppViewModel(
    private val authRepository: IUserRepository
) : BaseViewModel<AppState, AppUiAction>(){
    override val _state = MutableStateFlow(AppState())
    override fun doAction(action: AppUiAction) {
        when(action){
            is AppUiAction.CheckSession -> {
                viewModelScope.launch {
                    authRepository.checkSession().collectLatest{ isValid ->
                        _state.update { state ->
                            state.copy(isSessionValid = isValid)
                        }
                    }
                }
            }
        }
    }
}

data class AppState(
    val isSessionValid: Boolean? = null
)

sealed class AppUiAction{
    data object CheckSession: AppUiAction()
}