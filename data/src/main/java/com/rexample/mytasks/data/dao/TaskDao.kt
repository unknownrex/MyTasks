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

    @Query("DELETE FROM tasks WHERE id IN (:taskIds) AND user_id = :userId")
    suspend fun deleteTasks(taskIds: List<Int>, userId: Int)

    @Query("SELECT * FROM tasks WHERE user_id = :userId ORDER BY is_pinned DESC, date ASC, time ASC")
    suspend fun getAllTasks(userId: Int): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND name LIKE '%' || :query || '%'")
    suspend fun searchTasks(userId: Int, query: String): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE user_id = :userId AND category_id = :categoryId")
    suspend fun filterTasksByCategory(userId: Int, categoryId: Int): List<TaskEntity>

    @Query("UPDATE tasks SET is_pinned = :isPinned WHERE id = :taskId AND user_id = :userId")
    suspend fun pinTask(userId: Int, taskId: Int, isPinned: Boolean)

    @Query("UPDATE tasks SET is_done = :isDone WHERE id = :taskId AND user_id = :userId")
    suspend fun markAsDone(taskId: Int, userId: Int, isDone: Boolean)

    @Query("UPDATE tasks SET is_done = 1 WHERE id IN (:taskIds) AND user_id = :userId")
    suspend fun multipleMarkAsDone(userId: Int, taskIds: List<Int>)

}
