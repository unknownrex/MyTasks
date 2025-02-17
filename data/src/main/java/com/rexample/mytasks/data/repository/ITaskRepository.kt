package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ITaskRepository {
    fun insertTask(task: TaskEntity): Flow<Resource<Unit>>
    fun updateTask(task: TaskEntity): Flow<Resource<Unit>>
    fun deleteTask(task: TaskEntity): Flow<Resource<Unit>>
    fun deleteTasks(taskIds: List<Int>, userId: Int?): Flow<Resource<Unit>>
    fun getAllTasks(userId: Int?): Flow<Resource<List<TaskEntity>>>
    fun searchTasks(userId: Int?, query: String): Flow<Resource<List<TaskEntity>>>
    fun filterTasksByCategory(userId: Int?, categoryId: Int): Flow<Resource<List<TaskEntity>>>
    fun pinTask(userId: Int?, taskId: Int,  isPinned: Boolean): Flow<Resource<Unit>>
    fun markAsDone(userId: Int?, taskId: Int, isDone: Boolean): Flow<Resource<Unit>>
    fun multipleMarkAsDone(userId: Int?, taskId: List<Int>): Flow<Resource<Unit>>
    fun getTaskById(taskId: Int): Flow<Resource<TaskEntity>>
}
