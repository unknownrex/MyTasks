package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.UserEntity

interface IUserRepository {
    suspend fun register(user: UserEntity)
    suspend fun login(email: String, password: String): UserEntity?
    suspend fun getUserEmail(userId: Int): String?
}
