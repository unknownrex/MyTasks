package com.rexample.mytasks.ui.home.util

import com.rexample.mytasks.data.entity.TaskEntity
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun categorizeTasks(tasks: List<TaskEntity>): Map<String, List<TaskEntity>> {
    val today = LocalDate.now()
    val now = LocalTime.now()

    val dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    val completedTasks = tasks.filter { it.isDone }

    val overdueTasks = tasks.filter {
        !it.isDone && it.date?.let { dateStr ->
            val taskDate = LocalDate.parse(dateStr, dateFormatter)
            val taskTime = it.time?.let { timeStr -> LocalTime.parse(timeStr, timeFormatter) } ?: LocalTime.MIN

            taskDate.isBefore(today) || (taskDate == today && taskTime.isBefore(now))
        } == true
    }

    val pinnedTasks = tasks.filter { it.isPinned && !it.isDone }
    val finalPinnedTasks = pinnedTasks + overdueTasks.filter { it.isPinned && it !in pinnedTasks }


    val todayTasks = tasks.filter {
        !it.isDone && it !in overdueTasks && it !in finalPinnedTasks &&
                it.date?.let { dateStr ->
                    val taskDate = LocalDate.parse(dateStr, dateFormatter)
                    val taskTime = it.time?.let { timeStr -> LocalTime.parse(timeStr, timeFormatter) } ?: LocalTime.MIN

                    taskDate == today && taskTime.isAfter(now)
                } == true
    }

    val tomorrowTasks = tasks.filter {
        !it.isDone && it !in overdueTasks && it !in finalPinnedTasks &&
                it.date?.let { dateStr ->
                    LocalDate.parse(dateStr, dateFormatter) == today.plusDays(1)
                } == true
    }

    val upcomingTasks = tasks.filter {
        !it.isDone && it !in overdueTasks && it !in finalPinnedTasks &&
                it.date?.let { dateStr ->
                    LocalDate.parse(dateStr, dateFormatter).isAfter(today.plusDays(1))
                } == true
    }

    val filteredOverdueTasks = overdueTasks.filterNot { it.isPinned }

    return mapOf(
        "Disematkan" to finalPinnedTasks,
        "Hari ini" to todayTasks,
        "Besok" to tomorrowTasks,
        "Mendatang" to upcomingTasks,
        "Terlambat" to filteredOverdueTasks,
        "Selesai" to completedTasks
    )
}

