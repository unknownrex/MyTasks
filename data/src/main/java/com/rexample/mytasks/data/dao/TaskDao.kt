package com.rexample.mytasks.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.rexample.mytasks.data.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id IN (:taskIds)")
    suspend fun deleteTasks(taskIds: List<Int>)

    @Query("SELECT * FROM tasks ORDER BY is_pinned DESC, date ASC, time ASC")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE name LIKE '%' || :query || '%'")
    suspend fun searchTasks(query: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE category_id = :categoryId")
    suspend fun filterTasksByCategory(categoryId: Int): List<TaskEntity>

    @Query("UPDATE tasks SET is_pinned = :isPinned WHERE id = :taskId")
    suspend fun pinTask(taskId: Int, isPinned: Boolean)

    @Query("UPDATE tasks SET is_done = :isDone WHERE id = :taskId")
    suspend fun markAsDone(taskId: Int, isDone: Boolean)

    @Query("UPDATE tasks SET is_done = 1 WHERE id IN (:taskIds)")
    suspend fun multipleMarkAsDone(taskIds: List<Int>)

    @Query("SELECT * FROM tasks WHERE id = :taskId LIMIT 1")
    suspend fun getTaskById(taskId: Int): TaskEntity?
}

