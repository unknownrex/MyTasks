package com.rexample.mytasks.data.repository

import android.util.Log
import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.data.preference.AuthPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class UserRepository(
    private val userDao: UserDao,
    private val authPreference: AuthPreference
) : IUserRepository {
    override suspend fun register(user: UserEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            userDao.register(user)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mendaftar: \${e.message}"))
        }
    }

    override suspend fun getUserEmail(userId: Int?): Resource<String> {
        if (userId == null) {
            return Resource.Error("Sesi akun telah habis")
        }

        return try {
            val email = userDao.getUserEmail(userId)
            if (email != null) {
                Resource.Success(email)
            } else {
                Resource.Error("Email tidak ditemukan")
            }
        } catch (e: Exception) {
            Resource.Error("Gagal mengambil email: ${e.message}")
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

    override suspend fun logout(): Flow<Resource<Unit>> = flow{
        try {
            Log.d("ProfileViewModel", "logout() dipanggil")
            emit(Resource.Loading())

            Log.d("ProfileViewModel", "Memanggil clearUser()")
            authPreference.clearUser()

            Log.d("ProfileViewModel", "clearUser() selesai")
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            Log.e("ProfileViewModel", "Gagal logout: ${e.message}")
            emit(Resource.Error("Gagal logout: ${e.message}"))
        }
    }

    override suspend fun checkSession(): Flow<Boolean> = flow {
        val userId = authPreference.userId.first()

        if (userId == null) {
            emit(false)
            return@flow
        }

        val userExists = userDao.getUserById(userId) != null
        emit(userExists)
    }.catch {
        emit(false)
    }

    override suspend fun getUserById(userId: Int?): Flow<Resource<UserEntity>> = flow{
        if (userId == null) {
            emit(Resource.Error("Sesi akun telah habis"))
            return@flow
        }

        try {
            emit(Resource.Loading())
            val result = userDao.getUserById(userId)
            if (result != null) {
                emit(Resource.Success(result))
            } else {
                emit(Resource.Error("Gagal mengambil data user"))
            }

        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil data user"))
        }
    }

//    override suspend fun checkSession(): Flow<Boolean> {
//        return authPreference.isSessionValid()
//    }

    override fun login(email: String, password: String): Flow<Resource<UserEntity?>> = flow {
        try {
            emit(Resource.Loading())
            val user = userDao.login(email, password)
            if (user != null) {
                authPreference.saveUser(user.id)
                emit(Resource.Success(user))
            } else {
                emit(Resource.Error("Email atau password salah"))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Gagal login: ${e.message}"))
        }
    }
}


