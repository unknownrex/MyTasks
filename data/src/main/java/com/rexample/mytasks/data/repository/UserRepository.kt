package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.UserEntity

class UserRepository(private val userDao: UserDao) : IUserRepository {
    override suspend fun register(user: UserEntity) {
        userDao.register(user)
    }

    override suspend fun login(email: String, password: String): UserEntity? {
        return userDao.login(email, password)
    }

    override suspend fun getUserEmail(userId: Int): String? {
        return userDao.getUserEmail(userId)
    }
}