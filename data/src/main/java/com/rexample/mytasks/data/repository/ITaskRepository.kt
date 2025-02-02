package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity

interface ITaskRepository {
    suspend fun insertTask(task: TaskEntity)
    suspend fun updateTask(task: TaskEntity)
    suspend fun deleteTask(task: TaskEntity)
    suspend fun getAllTasks(userId: Int): List<TaskEntity>
    suspend fun searchTasks(userId: Int, query: String): List<TaskEntity>
    suspend fun filterTasksByCategory(userId: Int, categoryId: Int): List<TaskEntity>
    suspend fun pinTask(taskId: Int, userId: Int, isPinned: Boolean)
}
