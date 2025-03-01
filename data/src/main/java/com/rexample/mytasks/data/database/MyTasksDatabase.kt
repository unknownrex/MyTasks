package com.rexample.mytasks.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rexample.mytasks.data.dao.CategoryDao
import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.entity.TaskEntity

@Database(entities = [CategoryEntity::class, TaskEntity::class], version = 5, exportSchema = true)

abstract class MyTasksDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun taskDao(): TaskDao
}
