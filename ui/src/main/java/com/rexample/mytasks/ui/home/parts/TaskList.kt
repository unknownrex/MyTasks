package com.rexample.mytasks.ui.home.parts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.home.categoryDummies
import com.rexample.mytasks.ui.home.taskDummies

@Composable
fun TaskList(
    tasks: List<TaskEntity>,
    categories: List<CategoryEntity>,
    onEditTask: (TaskEntity) -> Unit, // Tambahkan navigasi ke halaman edit
    onLongPress: (Boolean) -> Unit,
    onPinClick: () -> Unit,
    onDoneClick: () -> Unit
) {
    var isMultiSelectMode by remember { mutableStateOf(false) }
    var selectedTasks by remember { mutableStateOf(setOf<Int>()) }

    LazyColumn(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        items(tasks) { task ->
            val categoryName = categories.find { it.id == task.categoryId }?.name

            TaskCard(
                dataTask = task,
                categoryName = categoryName,
                isSelected = selectedTasks.contains(task.id),
                onSelect = {
                    if (isMultiSelectMode) {
                        selectedTasks = if (selectedTasks.contains(task.id)) {
                            selectedTasks - task.id
                        } else {
                            selectedTasks + task.id
                        }

                        if (selectedTasks.isEmpty()) {
                            isMultiSelectMode = false
                            onLongPress(false)
                        }
                    } else {
                        onEditTask(task)
                    }
                },
                onLongPress = {
                    isMultiSelectMode = true
                    selectedTasks = selectedTasks + task.id
                    onLongPress(isMultiSelectMode)
                },
                onPinClick = { onPinClick() },
                onDoneClick = { onDoneClick() }
            )

        }
    }
}

@Composable
@Preview(showBackground = true)
private fun TaskListPreview() {
    MyTasksTheme {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TaskList(
                tasks = taskDummies,
                categories = categoryDummies,
                onEditTask = {},
                onLongPress = {},
                onPinClick = {},
                onDoneClick = {}
            )
        }
    }
}