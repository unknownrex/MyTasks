package com.rexample.mytasks.ui.edittask

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.preference.AuthPreference
import com.rexample.mytasks.data.repository.ICategoryRepository
import com.rexample.mytasks.data.repository.ITaskRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditTaskViewModel(
    val taskRepository: ITaskRepository,
    val categoryRepository: ICategoryRepository,
    val authPreference: AuthPreference
) : BaseViewModel<EditTaskUiState, EditTaskUiAction>() {
    override val _state = MutableStateFlow(EditTaskUiState())

    private val userId = authPreference.userId

    override fun doAction(action: EditTaskUiAction) {
        when (action) {
            is EditTaskUiAction.UpdateTaskNameInput -> _state.update { it.copy(taskNameInput = action.newInput) }
            is EditTaskUiAction.UpdateTaskDateInput -> _state.update { it.copy(taskDateInput = action.newDate) }
            is EditTaskUiAction.UpdateTaskTimeInput -> _state.update { it.copy(taskTimeInput = action.newTime) }
            is EditTaskUiAction.UpdateTaskCategoryInput -> _state.update { it.copy(taskCategoryInput = action.newCategory) }
            is EditTaskUiAction.SetCurrentData -> getCurrentTask(action.currentDataId)
            EditTaskUiAction.EditTask -> editTaskData()
            EditTaskUiAction.GetAllCategories -> loadCategories()
        }
    }

    private fun getCurrentTask(taskId: Int) {
        viewModelScope.launch {
            taskRepository.getTaskById(taskId).collectLatest { data ->
                _state.update {
                    it.copy(
                        currentData = data
                    )
                }
            }

            setAllDefaultInput()
        }
    }

    private fun setAllDefaultInput() {
        val currentData = state.value.currentData.data
        if (currentData != null) {
            _state.update {
                it.copy(

                    taskNameInput = currentData.name,
                    taskDateInput = currentData.date,
                    taskTimeInput = currentData.time,
                    taskCategoryInput = currentData.categoryId.toString(),
                )
            }
        }
    }

    private fun editTaskData() {
        viewModelScope.launch {
            val newTaskEdit = state.value.currentData.data?.copy(
                name = state.value.taskNameInput,
                date = state.value.taskDateInput,
                time = state.value.taskTimeInput,
                categoryId = state.value.taskCategoryInput.toInt()
            )
            if (newTaskEdit != null) {
                taskRepository.updateTask(
                    newTaskEdit
                ).collectLatest { result ->
                    _state.update { state ->
                        state.copy(
                            result = result
                        )
                    }
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories(
                userId = userId.first()
            ).collectLatest { categories ->
                _state.update { state ->
                    state.copy(
                        categoryData = categories
                    )
                }
            }
        }
    }
}

data class EditTaskUiState(
    val taskNameInput: String = "",
    val taskDateInput: String = "",
    val taskTimeInput: String = "",
    val taskCategoryInput: String = "",
    val currentData: Resource<TaskEntity> = Resource.Idle(),
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle(),
    val result: Resource<Unit> = Resource.Idle()
)

sealed class EditTaskUiAction {
    data class UpdateTaskNameInput(val newInput: String) : EditTaskUiAction()
    data class UpdateTaskDateInput(val newDate: String) : EditTaskUiAction()
    data class UpdateTaskTimeInput(val newTime: String) : EditTaskUiAction()
    data class UpdateTaskCategoryInput(val newCategory: String) : EditTaskUiAction()
    data object EditTask: EditTaskUiAction()
    data class SetCurrentData(val currentDataId: Int): EditTaskUiAction()
    data object GetAllCategories : EditTaskUiAction()
}