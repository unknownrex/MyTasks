package com.rexample.mytasks.ui.home.parts

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.gray
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.util.formatDate
import com.rexample.mytasks.ui.core.util.trimText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    dataTask: TaskEntity,
    categoryName: String?,
    onSelect: () -> Unit,
    onLongPress: () -> Unit,
    isSelected: Boolean =false,
    onPinClick: () -> Unit,
    onDoneClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("dd - MM - yyyy HH:mm")

    val currentDateTime = LocalDateTime.now()

    val taskDateTime = try {
        LocalDateTime.parse("${dataTask.date} ${dataTask.time}", formatter)
    } catch (e: Exception) {
        null
    }

    val isLate = taskDateTime?.isBefore(currentDateTime) == true

    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        shape = RoundedCornerShape(8.dp),
        border = if(isSelected) BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary) else null,
        modifier = modifier
            .padding(bottom = 8.dp)
            .combinedClickable(
                onClick = { onSelect() },
                onLongClick = {
                    onLongPress()
                }
            )
            .graphicsLayer(alpha = if (dataTask.isDone) 0.6f else 1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomToggleButton(isChecked = dataTask.isDone, onDoneClick)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = dataTask.name,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        style = trimText()
                    )

                    Spacer(Modifier.height(4.dp))

                    if (categoryName != null) {
                        Text(
                            text = categoryName,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = gray,
                            style = trimText()
                        )
                    }

                    Spacer(Modifier.height(4.dp))

                    val textColor = if (isLate) MaterialTheme.colorScheme.error else primary
                    Text(
                        text = "${formatDate(dataTask.date)}, ${dataTask.time}",
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = textColor,
                        style = trimText()
                    )
                }
            }

            IconButton(onClick = { onPinClick() }) {
                if (dataTask.isPinned) {
                    Image(
                        painter = painterResource(id = R.drawable.enabled_pin),
                        contentDescription = "Pin task"
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.disabled_pin),
                        contentDescription = "Pin task"
                    )
                }
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun CardPreview() {
    MyTasksTheme {
        var isChecked by remember {
            mutableStateOf(false)
        }
        var taskDummy by remember {
            mutableStateOf(
                TaskEntity(
                    id = 1,
                    name = "Mempelajari Kotlin",
                    date = "08 - 02 - 2025",
                    time = "15:30",
                    isDone = false,
                    isPinned = true,
                    categoryId = 1,
                    userId = 1
                )
            )
        }
        var isMultiSelectMode by remember { mutableStateOf(false) }
        var selectedTasks by remember { mutableStateOf(setOf<Int>()) }

        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            TaskCard(
                dataTask = taskDummy,
                categoryName = "Belajar",
                onSelect = {},
                onLongPress = {},
                isSelected = true,
                onDoneClick = {
                    taskDummy = taskDummy.copy(isPinned = !taskDummy.isDone)
                },
                onPinClick = {
                    taskDummy = taskDummy.copy(isPinned = !taskDummy.isPinned)
                }
            )
        }
    }
}