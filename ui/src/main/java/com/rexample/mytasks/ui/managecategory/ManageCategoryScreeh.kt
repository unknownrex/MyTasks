package com.rexample.mytasks.ui.managecategory

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.addnewtask.NewTaskUiAction
import com.rexample.mytasks.ui.core.appstate.error.EmptyView
import com.rexample.mytasks.ui.core.appstate.error.ErrorView
import com.rexample.mytasks.ui.core.appstate.loading.LoadingView
import com.rexample.mytasks.ui.core.dialog.ConfirmDeleteDialog
import com.rexample.mytasks.ui.core.parts.AppTextField
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.secondary
import com.rexample.mytasks.ui.core.theme.white
import com.rexample.mytasks.ui.home.categoryDummies
import org.koin.androidx.compose.koinViewModel

@Composable
fun ManageCategoryScreen(
    viewModel: ManageCategoryViewModel = koinViewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.state.collectAsState()
    val action = {action: ManageCategoryUiAction -> viewModel.doAction(action)}

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        action(ManageCategoryUiAction.LoadCategories)
    }

    Scaffold(
        topBar = { ManageCategoryTopBar()},
        floatingActionButton = {
            FloatingActionButton(
                onClick = { action(ManageCategoryUiAction.ShowAddDialog) },
                shape = CircleShape,
                containerColor = MaterialTheme.colorScheme.primary,
                elevation = FloatingActionButtonDefaults.elevation(1.dp),
                modifier = Modifier.size(60.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->

        when(state.categoryData) {
            is Resource.Error -> ErrorView(errorMessage = state.categoryData.message)
            is Resource.Success -> {
                val categoryList = state.categoryData.data ?: emptyList()

                if (categoryList.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        items(categoryList) { data ->
                            CategoryItem(
                                onEditClick = { action(ManageCategoryUiAction.ShowEditDialog(data)) },
                                onDeleteClick = { action(ManageCategoryUiAction.ShowDeleteDialog(data)) },
                                data = data
                            )
                        }
                    }
                } else {
                    EmptyView(
                        text = stringResource(R.string.click_plus_to_add_category)
                    )
                }
            }
            else -> LoadingView()
        }


        if (state.showEditDialog) {
            CategoryFormDialog(
                title = stringResource(id = R.string.edit_category),
                value = state.editCategoryNameInput,
                onValueChange = { action(ManageCategoryUiAction.UpdateEditCategoryInput(it)) },
                onDismissRequest = { action(ManageCategoryUiAction.HideEditDialog)},
                onConfirmClick = { action(ManageCategoryUiAction.EditCategory) },
                isConfirmEnabled = state.editCategoryNameInput.isNotEmpty()
            )
        }
        if(state.showAddDialog) {
            CategoryFormDialog(
                title = stringResource(id = R.string.add_category),
                value = state.categoryNameInput,
                onValueChange = { action(ManageCategoryUiAction.UpdateCategoryInput(it))},
                onDismissRequest = { action(ManageCategoryUiAction.HideAddDialog)},
                onConfirmClick = { action(ManageCategoryUiAction.AddCategory) },
                isConfirmEnabled = state.categoryNameInput.isNotEmpty()
            )
        }
        if(state.showDeleteDialog) {
            ConfirmDeleteDialog(
                title = "Hapus kategori?",
                text = "Apakah anda yakin untuk menghapus kategori ini?",
                onConfirmClick = { action(ManageCategoryUiAction.DeleteCategory) },
                onDismissRequest = { action(ManageCategoryUiAction.HideDeleteDialog)})
        }
    }
}

@Composable
fun CategoryFormDialog(
    value: String,
    title: String,
    onDismissRequest: () -> Unit,
    onValueChange: (String) -> Unit,
    onConfirmClick: () -> Unit,
    isConfirmEnabled: Boolean
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = white
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 14.dp, start = 14.dp, end = 14.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = black,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(16.dp))
                AppTextField(
                    singleLine = true,
                    label = {
                        Text(text = "Nama kategori")
                    },
                    value = value,
                    onValueChange = onValueChange
                )
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = onDismissRequest,
                    ) {
                        Text(
                            text = "BATAL",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                        )
                    }
                    TextButton(
                        onClick = onConfirmClick,
                        enabled = isConfirmEnabled
                    ) {
                        Text(
                            text = "TAMBAH",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoryTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "MyTasks",
                fontWeight = FontWeight.Bold,
                color = primary,
                fontSize = 24.sp
            )
        },
        colors = TopAppBarDefaults.topAppBarColors().copy(
            containerColor = secondary
        )
    )
}

@Composable
@Preview(showBackground = true)
fun ManageCategoryPreview() {
    MyTasksTheme {
        ManageCategoryScreen()
    }
}