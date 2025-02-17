package com.rexample.mytasks.ui.core.parts

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTimeFilled
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.secondary
import com.rexample.mytasks.ui.core.theme.white
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskForm(
    taskNameValue: String,
    onNameValueChange: (String) -> Unit,

    taskDateValue: String,
    onDateValueChange: (String) -> Unit,

    taskTimeValue: String,
    onTimeValueChange: (String) -> Unit,

    taskCategoryValue: String,
    onCategoryValueChange: (String) -> Unit,

    categoryData: List<CategoryEntity>,
) {
    var expanded by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val selectedCategory = categoryData.find { it.id.toString() == taskCategoryValue }
    var selectedCategoryName by remember { mutableStateOf(selectedCategory?.name ?: "Pilih kategori") }

    val (initialHour, initialMinute) = remember(taskTimeValue) {
        if (taskTimeValue.isNotEmpty()) {
            val parts = taskTimeValue.split(":")
            parts[0].toInt() to parts[1].toInt()
        } else {
            val now = LocalDateTime.now()
            now.hour to now.minute
        }
    }
    var selectedHour by remember(taskTimeValue) { mutableIntStateOf(initialHour) }
    var selectedMinute by remember(taskTimeValue) { mutableIntStateOf(initialMinute) }

    LaunchedEffect(Unit) {
        if (taskDateValue.isEmpty()) {
            val today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd - MM - yyyy"))
            onDateValueChange(today)
        }

        if (taskTimeValue.isEmpty()) {
            val now = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            onTimeValueChange(now)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        AppTextField(
            value = taskNameValue,
            onValueChange = onNameValueChange,
            label = {
                Text(
                    buildAnnotatedString {
                        append("Nama tugas")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append("*")
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(Icons.Filled.AssignmentTurnedIn, contentDescription = null)
            },
        )

        AppTextField(
            value = taskDateValue,
            onValueChange = {},
            label = {
                Text(
                    buildAnnotatedString {
                        append("Tanggal")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append("*")
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            trailingIcon = {
                Icon(Icons.Filled.CalendarMonth, contentDescription = null)
            },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = black,
                disabledTextColor = black,
                disabledTrailingIconColor = black,
                disabledIndicatorColor = black
            )
        )

        if (showDatePicker) {
            val dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
            val today = LocalDate.now()

            val initialDate = remember(taskDateValue) {
                if (taskDateValue.isNotEmpty()) {
                    LocalDate.parse(taskDateValue, dateFormatter)
                } else {
                    today
                }
            }

            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = initialDate.toEpochDay() * 24 * 60 * 60 * 1000
            )

            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val selectedDate = Instant.ofEpochMilli(it)
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                            onDateValueChange(selectedDate.format(dateFormatter))
                        }
                        showDatePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) { Text("BATAL") }
                },
            ) {
                DatePicker(
                    modifier = Modifier.size(500.dp),
                    title = {
                        Text(
                            "Pilih Tanggal",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 24.sp,
                            modifier = Modifier.padding(24.dp)
                        )
                    },
                    state = datePickerState,
                    colors = DatePickerDefaults.colors().copy(
                        todayContentColor = primary,
                        todayDateBorderColor = primary,
                        selectedDayContainerColor = primary,
                        selectedDayContentColor = white,
                        dayContentColor = black
                    )
                )
            }
        }


        AppTextField(
            value = "%02d:%02d".format(selectedHour, selectedMinute),
            onValueChange = {},
            label = {
                Text(
                    buildAnnotatedString {
                        append("Waktu")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.error)) {
                            append("*")
                        }
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showTimePicker = true },
            trailingIcon = {
                Icon(Icons.Filled.AccessTimeFilled, contentDescription = null)
            },
            enabled = false,
            readOnly = true,
            colors = TextFieldDefaults.colors(
                disabledContainerColor = Color.Transparent,
                disabledLabelColor = black,
                disabledTextColor = black,
                disabledTrailingIconColor = black,
                disabledIndicatorColor = black
            )
        )

        if (showTimePicker) {
            val timePickerState = rememberTimePickerState(
                initialHour = selectedHour,
                initialMinute = selectedMinute,
                is24Hour = true
            )
            AlertDialog(
                onDismissRequest = { showTimePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        selectedHour = timePickerState.hour
                        selectedMinute = timePickerState.minute
                        onTimeValueChange("%02d:%02d".format(selectedHour, selectedMinute))
                        showTimePicker = false
                    }) { Text("OK") }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePicker = false }) { Text("BATAL") }
                },
                title = { Text("Pilih Waktu", fontWeight = FontWeight.SemiBold) },
                text = {
                    TimePicker(
                        state = timePickerState,
                        modifier = Modifier.padding(16.dp),
                        colors = TimePickerDefaults.colors().copy(
                            selectorColor = primary,
                            clockDialColor = secondary,
                            timeSelectorSelectedContainerColor = primary,
                            timeSelectorUnselectedContainerColor = secondary,
                            timeSelectorUnselectedContentColor = black,
                            timeSelectorSelectedContentColor = white,
                            clockDialUnselectedContentColor = black,
                            clockDialSelectedContentColor = white
                        )
                    )
                }
            )
        }


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            AppTextField(
                value = selectedCategoryName,
                onValueChange = {},
                label = {
                    Text(text = stringResource(id = R.string.category))
                },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categoryData.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            selectedCategoryName = category.name
                            onCategoryValueChange(category.id.toString())
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}