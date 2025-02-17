package com.rexample.mytasks.ui.profile

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.preference.AuthPreference
import com.rexample.mytasks.data.repository.ITaskRepository
import com.rexample.mytasks.data.repository.IUserRepository
import com.rexample.mytasks.data.repository.UserRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import com.rexample.mytasks.ui.home.HomeUiAction
import com.rexample.mytasks.ui.profile.util.isOverdue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    val userRepository: IUserRepository,
    val taskRepository: ITaskRepository,
    val authPreference: AuthPreference
) : BaseViewModel< ProfileUiState , ProfileUiAction>() {
    override val _state = MutableStateFlow(ProfileUiState())
    override fun doAction(action: ProfileUiAction) {
        when(action) {
            ProfileUiAction.GetUserData -> getUserData()
            ProfileUiAction.Logout -> logout()
            ProfileUiAction.GetAllTasks -> getAllTasks()
        }
    }

    private fun getAllTasks() {
        viewModelScope.launch {
            taskRepository.getAllTasks(authPreference.userId.first()).collectLatest { data ->
                _state.update { state ->
                    state.copy(
                        taskData = data
                    )
                }
            }

            getTaskSummary()
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            val userId = authPreference.userId.first()
            userRepository.getUserById(userId).collectLatest { data ->
                _state.update { state ->
                    state.copy(
                        userData = data
                    )
                }
            }
        }
    }

    private fun logout() {
        viewModelScope.launch {
            userRepository.logout().collectLatest {
                _state.update { state ->
                    state.copy(
                        logoutResult = it
                    )
                }
            }
        }
    }

    fun getTaskSummary() {
        val tasks = state.value.taskData.data ?: emptyList()

        val completedTasks = tasks.count { it.isDone }
        val overdueTasks = tasks.count { it.isOverdue() }
        val totalTasks = tasks.size

        val completionRate = if (totalTasks > 0) {
            (completedTasks * 100) / totalTasks
        } else 0

        val taskSummary = TaskSummary(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            overdueTasks = overdueTasks,
            completionRate = completionRate
        )

        _state.update {
            it.copy(
                taskSummary = taskSummary
            )
        }
    }
}

data class ProfileUiState(
    val logoutResult: Resource<Unit> = Resource.Idle(),
    val taskData: Resource<List<TaskEntity>> = Resource.Idle(),
    val userData: Resource<UserEntity> = Resource.Idle(),
    val taskSummary: TaskSummary? = null
)

sealed class ProfileUiAction {
    data object GetAllTasks : ProfileUiAction()
    data object GetUserData: ProfileUiAction()
    data object Logout: ProfileUiAction()
}

data class TaskSummary(
    val totalTasks: Int,
    val completedTasks: Int,
    val overdueTasks: Int,
    val completionRate: Int
)