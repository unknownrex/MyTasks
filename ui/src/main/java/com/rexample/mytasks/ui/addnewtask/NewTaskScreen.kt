package com.rexample.mytasks.ui.addnewtask

import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.parts.AppButton
import com.rexample.mytasks.ui.core.parts.TaskForm
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.secondary
import com.rexample.mytasks.ui.home.HomeUiAction
import com.rexample.mytasks.ui.home.categoryDummies
import org.koin.androidx.compose.koinViewModel

@Composable
fun NewTaskScreen(
    viewModel: NewTaskViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val action = {action: NewTaskUiAction -> viewModel.doAction(action)}

    LaunchedEffect(Unit) {
        action(NewTaskUiAction.LoadCategories)
    }

    Scaffold(
        topBar = { NewTaskTopBar({ navigateBack() })},
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
                onNameValueChange = { action(NewTaskUiAction.UpdateTaskNameInput(it)) },
                taskDateValue = state.taskDateInput,
                onDateValueChange = { action(NewTaskUiAction.UpdateTaskDateInput(it)) },
                taskTimeValue = state.taskTimeInput,
                onTimeValueChange = { action(NewTaskUiAction.UpdateTaskTimeInput(it)) },
                taskCategoryValue = state.taskCategoryInput,
                onCategoryValueChange = { action(NewTaskUiAction.UpdateTaskCategoryInput(it)) },
                categoryData = state.categoryData.data ?: emptyList()
            )

            AppButton(
                text = stringResource(R.string.add_task),
                modifier = Modifier.fillMaxWidth(),
                enabled = state.taskNameInput.isNotEmpty() && state.taskDateInput.isNotEmpty()
                        && state.taskTimeInput.isNotEmpty(),
                onClick = {
                    action(NewTaskUiAction.AddTask)
                    navigateBack()
                }
            )
        }
    }
}





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTaskTopBar(
    navigateBack: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.new_task),
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

@Preview(showBackground = true)
@Composable
private fun ScreenPreview() {
    MyTasksTheme {
        NewTaskScreen(
            navigateBack = {}
        )
    }
}