package com.rexample.mytasks.ui.home

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.preference.AuthPreference
import com.rexample.mytasks.data.repository.ICategoryRepository
import com.rexample.mytasks.data.repository.ITaskRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    val taskRepository: ITaskRepository,
    val categoryRepository: ICategoryRepository,
    val authPreference: AuthPreference
) : BaseViewModel<HomeUiState, HomeUiAction>() {
    override val _state = MutableStateFlow(HomeUiState())

    override fun doAction(action: HomeUiAction) {
        when (action) {
            is HomeUiAction.GetAllTasks -> getAllTasks()
            is HomeUiAction.GetFilteredTasks -> getFilteredTasks(action.categoryId)
            is HomeUiAction.SearchTasks -> searchTasks()
            is HomeUiAction.PinTask -> pinTask(action.taskId)
            is HomeUiAction.MarkAsDone -> markAsDoneTask(action.taskId)
            is HomeUiAction.GetAllCategories -> getAllCategories()
            is HomeUiAction.EnterSelectMode -> enterSelectMode(action.taskId)
            is HomeUiAction.SelectTask -> selectTask(action.taskId)
            is HomeUiAction.ExitSelectMode -> {
                _state.update { it.copy(isSelectMode = false, multipleSelectedTasks = emptyList()) }
            }

            is HomeUiAction.EnterSearchMode -> {
                _state.update { it.copy(isSearchMode = true) }
            }

            is HomeUiAction.SearchInputing -> {
                _state.update { it.copy(searchInput = action.newSearch) }
            }

            is HomeUiAction.ExitSearchMode -> {
                _state.update { it.copy(isSearchMode = false) }
            }

            HomeUiAction.MarkAsDoneMultiple -> markAsDoneSelectedTasks()
            HomeUiAction.DeleteSelectedTasks -> deleteSelectedTasks()
            HomeUiAction.ShowErrorDialog -> {
                _state.update { it.copy(showErrorDialog = true) }
            }

            HomeUiAction.HideErrorDialog -> {
                _state.update { it.copy(showErrorDialog = false) }
            }

            is HomeUiAction.SelectCategoryFilter -> _state.update { it.copy(selectedCategory = action.categoryId) }
        }
    }

    private fun enterSelectMode(taskId: Int) {
        _state.update {
            it.copy(
                isSelectMode = true,
                multipleSelectedTasks = it.multipleSelectedTasks + taskId
            )
        }
    }

    private fun selectTask(taskId: Int) {
        _state.update {
            val newList = if (it.multipleSelectedTasks.contains(taskId)) {
                it.multipleSelectedTasks - taskId
            } else {
                it.multipleSelectedTasks + taskId
            }
            it.copy(
                multipleSelectedTasks = newList,
                isSelectMode = newList.isNotEmpty()
            )
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
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories(authPreference.userId.first())
                .collectLatest { data ->
                    _state.update { state ->
                        state.copy(
                            categoryData = data
                        )
                    }
                }
        }
    }

    private fun getFilteredTasks(categoryId: Int) {
        viewModelScope.launch {

            taskRepository.filterTasksByCategory(
                authPreference.userId.first(),
                categoryId
            ).collectLatest { data ->
                _state.update { state ->
                    state.copy(
                        taskData = data
                    )
                }
            }

        }
    }

    @OptIn(FlowPreview::class)
    private fun searchTasks() {
        viewModelScope.launch {
            taskRepository.searchTasks(
                authPreference.userId.first(),
                state.value.searchInput
            )
                .debounce(300)
                .collectLatest { data ->
                    _state.update { state ->
                        state.copy(
                            taskData = data
                        )
                    }
                }
        }
    }

    private fun pinTask(taskId: Int) {
        val currentTask = state.value.taskData.data?.find { it.id == taskId }
        val newPin = !(currentTask?.isPinned ?: false)

        viewModelScope.launch {
            taskRepository.pinTask(authPreference.userId.first(), taskId, newPin).collectLatest {
                _state.update { state ->
                    state.copy(
                        actionResult = it
                    )
                }
            }

            getAllTasks()
        }
    }

    private fun markAsDoneTask(taskId: Int) {
        val currentTask = state.value.taskData.data?.find { it.id == taskId }
        val newDoneTask = !(currentTask?.isDone ?: false)

        viewModelScope.launch {
            taskRepository.markAsDone(authPreference.userId.first(), taskId, newDoneTask)
                .collectLatest {
                    _state.update { state ->
                        state.copy(
                            actionResult = it
                        )
                    }
                }
            getAllTasks()
        }
    }

    private fun markAsDoneSelectedTasks() {
        viewModelScope.launch {
            val userId = authPreference.userId.first()
            val selectedTasks = state.value.multipleSelectedTasks

            if (selectedTasks.isNotEmpty()) {
                taskRepository.multipleMarkAsDone(userId, selectedTasks).collectLatest { result ->
                    _state.update {
                        it.copy(
                            actionResult = result,
                            multipleSelectedTasks = emptyList(),
                            isSelectMode = false
                        )
                    }
                }

                getAllTasks()
            } else {
                _state.update {
                    it.copy(
                        actionResult = Resource.Error(message = "Selected task is empty"),
                    )
                }
            }
        }
    }

    fun deleteSelectedTasks() {
        viewModelScope.launch {
            val userId = authPreference.userId.first()
            val selectedTasks = state.value.multipleSelectedTasks


            if (selectedTasks.isNotEmpty()) {
                taskRepository.deleteTasks(selectedTasks, userId).collectLatest { result ->
                    _state.update {
                        it.copy(
                            actionResult = result,
                            multipleSelectedTasks = emptyList(),
                            isSelectMode = false
                        )
                    }
                }

                getAllTasks()
            } else {
                _state.update {
                    it.copy(
                        actionResult = Resource.Error(message = "Selected task is empty"),
                    )
                }
            }
        }
    }
}

data class HomeUiState(
    val searchInput: String = "",
    val taskData: Resource<List<TaskEntity>> = Resource.Idle(),
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle(),
    val isSelectMode: Boolean = false,
    val isSearchMode: Boolean = false,
    val actionResult: Resource<Unit> = Resource.Idle(),
    val multipleSelectedTasks: List<Int> = emptyList(),
    val showErrorDialog: Boolean = false,
    val selectedCategory: Int? = null
)

sealed class HomeUiAction {
    data object GetAllTasks : HomeUiAction()
    data object GetAllCategories : HomeUiAction()
    data class GetFilteredTasks(val categoryId: Int) : HomeUiAction()
    data object SearchTasks : HomeUiAction()
    data class PinTask(val taskId: Int) : HomeUiAction()
    data class MarkAsDone(val taskId: Int) : HomeUiAction()
    data object MarkAsDoneMultiple : HomeUiAction()
    data class EnterSelectMode(val taskId: Int) : HomeUiAction()
    data class SelectTask(val taskId: Int) : HomeUiAction()
    data class SearchInputing(val newSearch: String) : HomeUiAction()
    data object ExitSelectMode : HomeUiAction()
    data object EnterSearchMode : HomeUiAction()
    data object ExitSearchMode : HomeUiAction()
    data object DeleteSelectedTasks : HomeUiAction()
    data object ShowErrorDialog : HomeUiAction()
    data object HideErrorDialog : HomeUiAction()
    data class SelectCategoryFilter(val categoryId: Int?)  : HomeUiAction()
}

val taskDummies = listOf(
    TaskEntity(
        id = 0,
        name = "Mempelajari Kotlin1",
        date = "08 - 02 - 2025",
        time = "15:30",
        isDone = false,
        isPinned = true,
        categoryId = 2,
        userId = 1
    ),
    TaskEntity(
        id = 0,
        name = "Mempelajari Kotlin2",
        date = "09 - 03 - 2025",
        time = "23:00",
        isDone = false,
        isPinned = true,
        categoryId = 2,
        userId = 1
    ),
    TaskEntity(
        id = 0,
        name = "Mempelajari Kotlin3",
        date = "09 - 02 - 2025",
        time = "23:00",
        isDone = false,
        isPinned = false,
        categoryId = 2,
        userId = 1
    ),
    TaskEntity(
        id = 1,
        name = "Pasang monitor4",
        date = "09 - 03 - 2025",
        time = "15:30",
        isDone = true,
        isPinned = true,
        categoryId = 0,
        userId = 1
    ),
    TaskEntity(
        id = 2,
        name = "Workout5",
        date = "12 - 03 - 2025",
        time = "15:30",
        isDone = false,
        isPinned = false,
        categoryId = 1,
        userId = 1
    )
)

val categoryDummies = listOf(
    CategoryEntity(
        id = 0,
        name = "Belajar",
        userId = 1
    ),
    CategoryEntity(
        id = 1,
        name = "Keinginan",
        userId = 1
    ),
    CategoryEntity(
        id = 2,
        name = "Kerja",
        userId = 1
    ),
    CategoryEntity(
        id = 3,
        name = "Sekolah",
        userId = 1
    )
)