package com.rexample.mytasks.ui.addnewtask

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.repository.ICategoryRepository
import com.rexample.mytasks.data.repository.ITaskRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class NewTaskViewModel(
    val taskRepository: ITaskRepository,
    val categoryRepository: ICategoryRepository,
) : BaseViewModel<NewTaskUiState, NewTaskUiAction>() {
    override val _state = MutableStateFlow(NewTaskUiState())

    override fun doAction(action: NewTaskUiAction) {
        when (action) {
            is NewTaskUiAction.UpdateTaskNameInput -> _state.update { it.copy(taskNameInput = action.newInput) }
            is NewTaskUiAction.UpdateTaskDateInput -> _state.update { it.copy(taskDateInput = action.newDate) }
            is NewTaskUiAction.UpdateTaskTimeInput -> _state.update { it.copy(taskTimeInput = action.newTime) }
            is NewTaskUiAction.UpdateTaskCategoryInput -> _state.update { it.copy(taskCategoryInput = action.newCategory) }
            is NewTaskUiAction.LoadCategories -> loadCategories()
            NewTaskUiAction.AddTask -> addTaskData()
        }
    }

    private fun addTaskData() {
        viewModelScope.launch {
            taskRepository.insertTask(
                TaskEntity(
                    name = state.value.taskNameInput,
                    date = state.value.taskDateInput,
                    time = state.value.taskTimeInput,
                    categoryId = if(state.value.taskCategoryInput.isEmpty()) null else state.value.taskCategoryInput.toInt(),
                )
            ).collectLatest { result ->
                _state.update { state ->
                    state.copy(
                        result = result
                    )
                }
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collectLatest { categories ->
                _state.update { state ->
                    state.copy(
                        categoryData = categories
                    )
                }
            }
        }
    }
}

data class NewTaskUiState(
    val taskNameInput: String = "",
    val taskDateInput: String = "",
    val taskTimeInput: String = "",
    val taskCategoryInput: String = "",
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle(),
    val result: Resource<Unit> = Resource.Idle()
)

sealed class NewTaskUiAction {
    data class UpdateTaskNameInput(val newInput: String) : NewTaskUiAction()
    data class UpdateTaskDateInput(val newDate: String) : NewTaskUiAction()
    data class UpdateTaskTimeInput(val newTime: String) : NewTaskUiAction()
    data class UpdateTaskCategoryInput(val newCategory: String) : NewTaskUiAction()
    data object AddTask: NewTaskUiAction()
    data object LoadCategories: NewTaskUiAction()
}
