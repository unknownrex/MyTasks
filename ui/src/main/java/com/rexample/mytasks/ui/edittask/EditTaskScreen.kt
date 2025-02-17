package com.rexample.mytasks.ui.edittask

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.parts.AppButton
import com.rexample.mytasks.ui.core.parts.TaskForm
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.secondary
import org.koin.androidx.compose.koinViewModel

@Composable
fun EditTaskScreen(
    taskId: Int,
    navigateBack: () -> Unit,
    viewModel: EditTaskViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
) {
    val state by viewModel.state.collectAsState()
    val action = {action: EditTaskUiAction -> viewModel.doAction(action)}
    LaunchedEffect(Unit) {
        action(EditTaskUiAction.GetAllCategories)
        action(EditTaskUiAction.SetCurrentData(taskId))
    }

    Scaffold(
        topBar = { EditTaskTopBar({ navigateBack() }) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TaskForm(
                taskNameValue = state.taskNameInput,
                onNameValueChange = { action(EditTaskUiAction.UpdateTaskNameInput(it)) },
                taskDateValue = state.taskDateInput,
                onDateValueChange = { action(EditTaskUiAction.UpdateTaskDateInput(it)) },
                taskTimeValue = state.taskTimeInput,
                onTimeValueChange = { action(EditTaskUiAction.UpdateTaskTimeInput(it)) },
                taskCategoryValue = state.taskCategoryInput,
                onCategoryValueChange = { action(EditTaskUiAction.UpdateTaskCategoryInput(it)) },
                categoryData = state.categoryData.data ?: emptyList()
            )

            AppButton(
                text = "Simpan",
                modifier = Modifier.fillMaxWidth(),
                enabled = state.taskNameInput.isNotEmpty() && state.taskDateInput.isNotEmpty()
                        && state.taskTimeInput.isNotEmpty(),
                onClick = {
                    action(EditTaskUiAction.EditTask)
                    navigateBack()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.edit_task),
                fontWeight = FontWeight.Medium,
                fontSize = 18.sp,
                color = black
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = secondary
        ),
        navigationIcon = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back),
                    tint = black
                )
            }
        }
    )
}