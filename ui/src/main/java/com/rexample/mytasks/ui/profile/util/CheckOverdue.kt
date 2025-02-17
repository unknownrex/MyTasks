package com.rexample.mytasks.ui.profile.util

import com.rexample.mytasks.data.entity.TaskEntity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun TaskEntity.isOverdue(): Boolean {
    if (date.isNullOrEmpty() || time.isNullOrEmpty()) return false

    val dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    return try {
        val taskDate = LocalDate.parse(date, dateFormatter)
        val taskTime = LocalTime.parse(time, timeFormatter)
        val taskDateTime = LocalDateTime.of(taskDate, taskTime)

        taskDateTime.isBefore(LocalDateTime.now()) && !isDone
    } catch (e: Exception) {
        false
    }
}
