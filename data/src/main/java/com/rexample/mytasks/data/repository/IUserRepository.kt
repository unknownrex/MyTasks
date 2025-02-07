package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun register(user: UserEntity): Resource<Unit>
    fun login(email: String, password: String): Flow<Resource<UserEntity?>>
    suspend fun getUserEmail(userId: Int): Resource<String?>
    suspend fun isEmailUsed(email: String): Resource<Boolean>
    suspend fun logout(): Resource<Unit>
    suspend fun checkSession(): Flow<Boolean>
}
