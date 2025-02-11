package com.rexample.mytasks.ui.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

abstract class BaseViewModel<UiState, UIAction>: ViewModel() {

    protected abstract val _state: MutableStateFlow<UiState>
    val state get() = _state.asStateFlow()

    abstract fun doAction(action: UIAction)

    fun <T> Flow<T>.launchCollectLatest(callback: (value: T) -> Unit){
        viewModelScope.launch {
            this@launchCollectLatest.distinctUntilChanged().collectLatest {
                callback(it)
            }
        }
    }

    fun <T> Flow<T>.launchCollect(callback: (value: T) -> Unit){
        viewModelScope.launch {
            this@launchCollect.distinctUntilChanged().collect {
                callback(it)
            }
        }
    }
}