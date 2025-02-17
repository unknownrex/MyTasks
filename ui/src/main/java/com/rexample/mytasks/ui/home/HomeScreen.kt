package com.rexample.mytasks.ui.home

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ModifierInfo
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.ui.core.appstate.error.EmptyView
import com.rexample.mytasks.ui.core.appstate.error.ErrorView
import com.rexample.mytasks.ui.core.appstate.loading.LoadingView
import com.rexample.mytasks.ui.core.dialog.ErrorDialog
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.home.parts.CategoryButtonList
import com.rexample.mytasks.ui.home.parts.TaskCard
import com.rexample.mytasks.ui.home.parts.TotalTaskIndicator
import com.rexample.mytasks.ui.home.parts.topbar.AppTopBar
import com.rexample.mytasks.ui.home.parts.topbar.MultipleSelectTopBar
import com.rexample.mytasks.ui.home.parts.topbar.SearchTopBar
import com.rexample.mytasks.ui.home.util.categorizeTasks
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel(),
    onEditTask: (Int) -> Unit,
    navigateCategory: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val tasks = state.taskData.data
    val groupedTasks = tasks?.let { categorizeTasks(it) }
    val action = { action: HomeUiAction -> viewModel.doAction(action) }
    val categories = state.categoryData.data ?: emptyList()

    Log.d("HomeScreen", "List: ${state.taskData.data}")
    Log.d("HomeScreen", "Result: ${state.actionResult} | ${state.actionResult.message}")

    LaunchedEffect(key1 = Unit) {
        action(HomeUiAction.GetAllTasks)
        action(HomeUiAction.GetAllCategories)
        if (state.actionResult is Resource.Error) action(HomeUiAction.ShowErrorDialog)
    }

    Scaffold(
        topBar = {
            if (state.isSelectMode) {
                MultipleSelectTopBar(
                    navigateBack = { action(HomeUiAction.ExitSelectMode) },
                    onDoneClick = { action(HomeUiAction.MarkAsDoneMultiple) },
                    onDeleteClick = { action(HomeUiAction.DeleteSelectedTasks) },
                    totalSelectedTasks = state.multipleSelectedTasks.size.toString()
                )
            } else if (state.isSearchMode) {
                SearchTopBar(
                    navigateBack = { action(HomeUiAction.ExitSearchMode) },
                    onValueChange = {
                        action(HomeUiAction.SearchInputing(it))
                        action(HomeUiAction.SearchTasks)
                    },
                    value = state.searchInput
                )
            } else {
                AppTopBar(
                    onSearchClick = { action(HomeUiAction.EnterSearchMode) },
                    navigateCategory = { navigateCategory() }
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {
            CategoryButtonList(
                categories = categories,
                selectedCategoryId = state.selectedCategory,
                onCategorySelected = { categoryId ->
                    action(HomeUiAction.SelectCategoryFilter(categoryId))
                    if (categoryId == null) {
                        action(HomeUiAction.GetAllTasks)
                    } else {
                        action(HomeUiAction.GetFilteredTasks(categoryId))
                    }
                }
            )

            when (state.taskData) {
                is Resource.Error -> {
                    ErrorView(errorMessage = state.taskData.message)
                }

                is Resource.Success -> {
                    if (state.taskData.data != emptyList<TaskEntity>()) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            if (groupedTasks != null) {
                                groupedTasks.forEach { (title, taskList) ->
                                    if (taskList.isNotEmpty()) {
                                        item {
                                            var isExpanded by remember { mutableStateOf(true) }

                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(vertical = 8.dp)
                                                        .clickable { isExpanded = !isExpanded },
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Text(
                                                        text = title,
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 18.sp,
                                                        color = black
                                                    )

                                                    Spacer(Modifier.width(4.dp))

                                                    TotalTaskIndicator(text = taskList.size.toString())

                                                    Icon(
                                                        imageVector = if (isExpanded) {
                                                            Icons.Default.ArrowDropUp
                                                        } else {
                                                            Icons.Default.ArrowDropDown
                                                        },
                                                        contentDescription = null
                                                    )
                                                }

                                                if (isExpanded) {
                                                    taskList.forEach { task ->
                                                        val categoryName =
                                                            categories.find { it.id == task.categoryId }?.name
                                                        val isSelected =
                                                            state.multipleSelectedTasks.contains(task.id)

                                                        TaskCard(
                                                            dataTask = task,
                                                            categoryName = categoryName,
                                                            isSelected = isSelected,
                                                            onSelect = {
                                                                if (state.isSelectMode) {
                                                                    action(HomeUiAction.SelectTask(task.id))
                                                                } else {
                                                                    onEditTask(task.id)
                                                                }
                                                            },
                                                            onLongPress = {
                                                                action(HomeUiAction.EnterSelectMode(task.id))
                                                            },
                                                            onPinClick = {
                                                                action(
                                                                    HomeUiAction.PinTask(
                                                                        task.id
                                                                    )
                                                                )
                                                            },
                                                            onDoneClick = {
                                                                action(
                                                                    HomeUiAction.MarkAsDone(
                                                                        task.id
                                                                    )
                                                                )
                                                            }
                                                        )
                                                    }
                                                }
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        EmptyView()
                    }
                }

                else -> {
                    LoadingView()
                }
            }
            if (state.showErrorDialog) {
                ErrorDialog(
                    errorMessage = state.actionResult.message,
                    onConfirmClick = { action(HomeUiAction.HideErrorDialog) },
                    onDismissRequest = { action(HomeUiAction.HideErrorDialog) }
                )
            }
        }
    }
}





/*

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    MyTasksTheme {
        HomeScreen(
            taskDummies,
            categoryDummies
        )
    }
}*/
