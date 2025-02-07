package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun insertTask(task: TaskEntity): Flow<Resource<Unit>>
    fun updateTask(task: TaskEntity): Flow<Resource<Unit>>
    fun deleteTask(task: TaskEntity): Flow<Resource<Unit>>
    fun getAllTasks(userId: Int): Flow<Resource<List<TaskEntity>>>
    fun searchTasks(userId: Int, query: String): Flow<Resource<List<TaskEntity>>>
    fun filterTasksByCategory(userId: Int, categoryId: Int): Flow<Resource<List<TaskEntity>>>
    fun pinTask(taskId: Int, userId: Int, isPinned: Boolean): Flow<Resource<Unit>>
}
