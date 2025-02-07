package com.rexample.mytasks.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rexample.mytasks.data.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun register(user: UserEntity)

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun login(email: String, password: String): UserEntity?

    @Query("SELECT email FROM users WHERE id = :userId")
    suspend fun getUserEmail(userId: Int): String?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun isEmailUsed(email: String): UserEntity?
}
