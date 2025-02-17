package com.rexample.mytasks.data.repository

import android.content.Context
import android.util.Log
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.entity.UserEntity
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
    private val context: Context
) : ITaskRepository {

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


    override fun insertTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        if (task.userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }
        try {
            emit(Resource.Loading())
            taskDao.insertTask(task)

            scheduleTaskReminder(task)

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menambahkan tugas: ${e.message}"))
        }
    }


    override fun updateTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.updateTask(task)

            WorkManager.getInstance(context).cancelUniqueWork("TaskReminder_${task.id}")

            scheduleTaskReminder(task)

            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memperbarui tugas: ${e.message}"))
        }
    }

    override fun deleteTask(task: TaskEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            taskDao.deleteTask(task)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghapus tugas: ${e.message}"))
        }
    }

    override fun deleteTasks(taskIds: List<Int>, userId: Int?): Flow<Resource<Unit>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            taskDao.deleteTasks(
                taskIds = taskIds,
                userId = userId
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghapus tugas: ${e.message}"))
        }
    }

    override fun getAllTasks(userId: Int?): Flow<Resource<List<TaskEntity>>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            val result = taskDao.getAllTasks(userId)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil daftar tugas: ${e.message}"))
        }
    }

    override fun searchTasks(userId: Int?, query: String): Flow<Resource<List<TaskEntity>>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            val result = taskDao.searchTasks(userId, query)
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mencari tugas: ${e.message}"))
        }
    }

    override fun filterTasksByCategory(userId: Int?, categoryId: Int): Flow<Resource<List<TaskEntity>>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            val result = taskDao.filterTasksByCategory(
                userId =  userId,
                categoryId = categoryId
            )
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memfilter tugas: ${e.message}"))
        }
    }

    override fun pinTask(userId: Int?, taskId: Int, isPinned: Boolean): Flow<Resource<Unit>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            taskDao.pinTask(
                userId = userId,
                taskId = taskId,
                isPinned = isPinned
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menyematkan tugas: ${e.message}"))
        }
    }

    override fun markAsDone(userId: Int?, taskId: Int, isDone: Boolean): Flow<Resource<Unit>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            taskDao.markAsDone(
                taskId = taskId,
                userId = userId,
                isDone = isDone
            )
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menyematkan tugas: ${e.message}"))
        }
    }

    override fun multipleMarkAsDone(userId: Int?, taskIds: List<Int>): Flow<Resource<Unit>> = flow {
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            taskDao.multipleMarkAsDone(
                userId = userId,
                taskIds = taskIds
            )
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
            emit(Resource.Error("Gagal menandai tugas sebagai selesai"))
        }
    }
}


