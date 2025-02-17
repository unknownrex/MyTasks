package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.TaskEntity

interface ITaskReminderRepository {
    fun scheduleTaskReminder(task: TaskEntity)
    fun deleteTaskReminder(taskId: Int)
}