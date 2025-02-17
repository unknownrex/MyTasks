package com.rexample.mytasks.ui.home.parts.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleSelectTopBar(
    navigateBack: () -> Unit,
    onDoneClick: () -> Unit,
    onDeleteClick: () -> Unit,
    totalSelectedTasks: String,
) {
    TopAppBar(
        title = {
            Text(
                text = totalSelectedTasks,
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
        },
        actions = {
            IconButton(onClick = { onDoneClick() }) {
                Icon(Icons.Filled.CheckCircle, null, tint = black)
            }
            IconButton(onClick = { onDeleteClick() }) {
                Icon(Icons.Filled.Delete, null, tint = black)
            }
        }
    )
}

@Composable
@Preview
private fun TopBarPreview() {
    MyTasksTheme {
        MultipleSelectTopBar(
            {},
            onDoneClick = {},
            onDeleteClick = {},
            totalSelectedTasks = "3"
        )
    }
}