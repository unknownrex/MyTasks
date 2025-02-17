package com.rexample.mytasks.ui.managecategory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.home.categoryDummies

@Composable
fun CategoryItem(
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    data: CategoryEntity
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Circle,
                contentDescription = null,
                modifier = Modifier.size(8.dp),
                tint = black
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                data.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = black
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { onEditClick() }) {
                Icon(
                    Icons.Filled.Edit, stringResource(R.string.edit_category),
                    tint = black
                )
            }
            IconButton(onClick = { onDeleteClick() }) {
                Icon(
                    Icons.Filled.Delete, stringResource(R.string.delete_category),
                    tint = black
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun CategoryItemPreview() {
    MyTasksTheme {
        CategoryItem(
            {},
            {},
            categoryDummies[1]
        )
    }
}