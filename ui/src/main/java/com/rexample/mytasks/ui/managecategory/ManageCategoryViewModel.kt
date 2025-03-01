package com.rexample.mytasks.ui.managecategory

import androidx.lifecycle.viewModelScope
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.repository.ICategoryRepository
import com.rexample.mytasks.ui.core.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ManageCategoryViewModel (
    val categoryRepository: ICategoryRepository
): BaseViewModel<ManageCategoryUiState, ManageCategoryUiAction>() {
    override val _state = MutableStateFlow(ManageCategoryUiState())

    override fun doAction(action: ManageCategoryUiAction) {
        when (action) {
            is ManageCategoryUiAction.UpdateCategoryInput -> _state.update { it.copy(categoryNameInput = action.newInput) }
            is ManageCategoryUiAction.UpdateEditCategoryInput -> _state.update { it.copy(editCategoryNameInput = action.newInput) }
            is ManageCategoryUiAction.ShowEditDialog -> _state.update {
                it.copy(showEditDialog = true, selectedCategory = action.category, editCategoryNameInput = action.category.name)
            }
            is ManageCategoryUiAction.ShowDeleteDialog -> _state.update { it.copy(showDeleteDialog = true, selectedCategory = action.category) }
            is ManageCategoryUiAction.EditCategory -> editCategory()
            is ManageCategoryUiAction.DeleteCategory -> deleteCategory()
            ManageCategoryUiAction.LoadCategories -> loadCategories()
            ManageCategoryUiAction.AddCategory -> addCategory()
            ManageCategoryUiAction.ShowAddDialog -> _state.update { it.copy(showAddDialog = true) }
            ManageCategoryUiAction.HideAddDialog -> _state.update { it.copy(showAddDialog = false) }
            ManageCategoryUiAction.HideDeleteDialog -> _state.update { it.copy(showDeleteDialog = false) }
            ManageCategoryUiAction.HideEditDialog -> _state.update { it.copy(showEditDialog = false) }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collectLatest { data ->
                _state.update {
                    it.copy(
                        categoryData = data
                    )
                }
            }
        }
    }

    private fun addCategory() {
        viewModelScope.launch {
            val newCategory = CategoryEntity(
                name = state.value.categoryNameInput,
            )

            categoryRepository.insertCategory(newCategory).collectLatest { result ->
                _state.update { state ->
                    state.copy(
                        actionResult = result,
                        categoryNameInput = "",
                        showAddDialog = false
                    )
                }
            }

            loadCategories()
        }
    }

    private fun editCategory() {
        viewModelScope.launch {
            val currentCategory = state.value.selectedCategory
            val newCategory = currentCategory?.copy(
                name = state.value.editCategoryNameInput,
            )

            if (newCategory != null) {
                categoryRepository.updateCategory(
                    category = newCategory
                ).collectLatest { result ->
                    _state.update { state ->
                        state.copy(
                            actionResult = result,
                            editCategoryNameInput = "",
                            showEditDialog = false
                        )
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        actionResult = Resource.Error("Gagal mengedit kategori")
                    )
                }
            }

            loadCategories()
        }
    }
    private fun deleteCategory() {
        viewModelScope.launch {
            val currentCategory = state.value.selectedCategory

            if (currentCategory != null) {
                categoryRepository.deleteCategory(currentCategory).collectLatest { result ->
                    _state.update { state ->
                        state.copy(
                            actionResult = result,
                            showDeleteDialog = false
                        )
                    }
                }
            } else {
                _state.update {
                    it.copy(
                        actionResult = Resource.Error("Gagal menghapus kategori")
                    )
                }
            }

            loadCategories()
        }
    }
}


data class ManageCategoryUiState(
    val actionResult: Resource<Unit> = Resource.Idle(),
    val selectedCategory: CategoryEntity? = null,
    val categoryNameInput: String = "",
    val editCategoryNameInput: String = "",
    val showAddDialog: Boolean = false,
    val showEditDialog: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val categoryData: Resource<List<CategoryEntity>> = Resource.Idle()
)

sealed class ManageCategoryUiAction {
    data class UpdateCategoryInput(val newInput: String) : ManageCategoryUiAction()
    data class UpdateEditCategoryInput(val newInput: String) : ManageCategoryUiAction()
    data class ShowEditDialog(val category: CategoryEntity) : ManageCategoryUiAction()
    data class ShowDeleteDialog(val category: CategoryEntity) : ManageCategoryUiAction()
    data object EditCategory : ManageCategoryUiAction()
    data object DeleteCategory: ManageCategoryUiAction()
    data object LoadCategories : ManageCategoryUiAction()
    data object AddCategory : ManageCategoryUiAction()
    data object ShowAddDialog : ManageCategoryUiAction()
    data object HideAddDialog : ManageCategoryUiAction()
    data object HideDeleteDialog : ManageCategoryUiAction()
    data object HideEditDialog : ManageCategoryUiAction()
}