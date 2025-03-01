package com.rexample.mytasks.ui.home.parts.topbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    onSearchClick: () -> Unit,
) {
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
        ),
        actions = {
            IconButton(onClick = { onSearchClick() }) {
                Icon(
                    Icons.Filled.Search, stringResource(R.string.search),
                    tint = black
                )
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    MyTasksTheme {
        AppTopBar({})
    }
}