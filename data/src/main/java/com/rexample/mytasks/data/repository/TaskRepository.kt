package com.rexample.mytasks.data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.workmanager.TaskReminderWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class TaskRepository(
    private val taskDao: TaskDao,
    private val taskReminderRepository: ITaskReminderRepository,
) : ITaskRepository {

    override fun insertTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.insertTask(task)
            taskReminderRepository.scheduleTaskReminder(task)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menambahkan tugas: ${e.message}"))
        }
    }

    override fun updateTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.updateTask(task)
            taskReminderRepository.deleteTaskReminder(task.id)
            taskReminderRepository.scheduleTaskReminder(task)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memperbarui tugas: ${e.message}"))
        }
    }

    override fun deleteTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.deleteTask(task)
            taskReminderRepository.deleteTaskReminder(task.id)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghapus tugas: ${e.message}"))
        }
    }

    override fun deleteTasks(taskIds: List<Int>): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.deleteTasks(taskIds)
            taskIds.forEach { taskReminderRepository.deleteTaskReminder(it) }
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghapus tugas: ${e.message}"))
        }
    }

    override fun getAllTasks(): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val result = taskDao.getAllTasks()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil daftar tugas: ${e.message}"))
        }
    }

    override fun searchTasks(query: String): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val result = taskDao.searchTasks(query)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mencari tugas: ${e.message}"))
        }
    }

    override fun filterTasksByCategory(categoryId: Int): Flow<Resource<List<TaskEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val result = taskDao.filterTasksByCategory(categoryId)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memfilter tugas: ${e.message}"))
        }
    }

    override fun pinTask(taskId: Int, isPinned: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.pinTask(taskId, isPinned)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menyematkan tugas: ${e.message}"))
        }
    }

    override fun markAsDone(taskId: Int, isDone: Boolean): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.markAsDone(taskId, isDone)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menandai tugas sebagai selesai"))
        }
    }

    override fun multipleMarkAsDone(taskIds: List<Int>): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.multipleMarkAsDone(taskIds)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menandai tugas sebagai selesai"))
        }
    }

    override fun getTaskById(taskId: Int): Flow<Resource<TaskEntity>> = flow {
        try {
            emit(Resource.Loading())
            val taskData = taskDao.getTaskById(taskId)
            emit(Resource.Success(taskData))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mendapatkan tugas"))
        }
    }
}

