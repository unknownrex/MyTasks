package com.rexample.mytasks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rexample.mytasks.data.dao.CategoryDao
import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity
import com.rexample.mytasks.data.entity.UserEntity

@Database(entities = [UserEntity::class, CategoryEntity::class, TaskEntity::class], version = 1, exportSchema = false)
abstract class MyTasksDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}
