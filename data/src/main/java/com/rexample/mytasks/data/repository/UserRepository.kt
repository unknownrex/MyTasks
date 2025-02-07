package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val userDao: UserDao,
    private val authPreference: AuthPreference
) : IUserRepository {
    override suspend fun register(user: UserEntity): Resource<Unit> {
        return try {
            userDao.register(user)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Gagal mendaftar: \${e.message}")
        }
    }

    override suspend fun getUserEmail(userId: Int): Resource<String?> {
        return try {
            val email = userDao.getUserEmail(userId)
            if (email != null) {
                Resource.Success(email)
            } else {
                Resource.Error("Email tidak ditemukan")
            }
        } catch (e: Exception) {
            Resource.Error("Gagal mengambil email: \${e.message}")
        }
    }

    override suspend fun isEmailUsed(email: String): Resource<Boolean> {
        return try {
            val user = userDao.isEmailUsed(email)
            Resource.Success(user != null)
        } catch (e: Exception) {
            Resource.Error("Gagal mengecek email: \${e.message}")
        }
    }

    override suspend fun logout(): Resource<Unit> {
        return try {
            authPreference.clearUser()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Gagal logout: \${e.message}")
        }
    }

    override suspend fun checkSession(): Flow<Boolean> {
        return authPreference.isSessionValid()
    }

    override fun login(email: String, password: String): Flow<Resource<UserEntity?>> = flow {
        try {
            emit(Resource.Loading())
            val user = userDao.login(email, password)
            if (user != null) {
                authPreference.saveUser(user.id, user.email)
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Email atau password salah"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Gagal login: \${e.message}"))
        }
    }
}


