package com.rexample.mytasks.data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.workmanager.TaskReminderWorker
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class TaskReminderRepository(
    private val context: Context
) : ITaskReminderRepository {
    override fun scheduleTaskReminder(task: TaskEntity) {
        val dateFormatter = DateTimeFormatter.ofPattern("dd - MM - yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

        val taskDate = LocalDate.parse(task.date, dateFormatter)
        val taskTime = LocalTime.parse(task.time, timeFormatter)
        val taskDateTime = LocalDateTime.of(taskDate, taskTime).minusMinutes(2)

        val delay = Duration.between(LocalDateTime.now(), taskDateTime).toMillis()

        if (delay > 0) {
            Log.e("TaskReminder", "Waktu tugas telah berlalu, tidak menjadwalkan pengingat")
            val workRequest = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        "TASK_NAME" to task.name,
                        "TASK_ID" to task.id,
                        "USER_ID" to (task.userId ?: 0)
                    )
                )
                .build()

            WorkManager
                .getInstance(context)
                .enqueueUniqueWork(
                    "TaskReminder_${task.id}",
                    ExistingWorkPolicy.REPLACE,
                    workRequest)

        }
    }

    override fun deleteTaskReminder(taskId: Int) {
        WorkManager.getInstance(context).cancelUniqueWork("TaskReminder_${taskId}")
    }
}