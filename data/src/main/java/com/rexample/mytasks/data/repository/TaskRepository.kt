package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.entity.UserEntity

class TaskRepository(private val taskDao: TaskDao) : ITaskRepository {
    override suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    override suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

    override suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }

    override suspend fun getAllTasks(userId: Int): List<TaskEntity> {
        return taskDao.getAllTasks(userId)
    }

    override suspend fun searchTasks(userId: Int, query: String): List<TaskEntity> {
        return taskDao.searchTasks(userId, query)
    }

    override suspend fun filterTasksByCategory(userId: Int, categoryId: Int): List<TaskEntity> {
        return taskDao.filterTasksByCategory(userId, categoryId)
    }

    override suspend fun pinTask(taskId: Int, userId: Int, isPinned: Boolean) {
        taskDao.pinTask(taskId, userId, isPinned)
    }
}
