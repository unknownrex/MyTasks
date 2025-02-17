package com.rexample.mytasks.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.rexample.mytasks.data.dao.CategoryDao
import com.rexample.mytasks.data.dao.TaskDao
import com.rexample.mytasks.data.dao.UserDao
import com.rexample.mytasks.data.database.MyTasksDatabase
import com.rexample.mytasks.data.preference.AuthPreference
import com.rexample.mytasks.data.repository.CategoryRepository
import com.rexample.mytasks.data.repository.ICategoryRepository
import com.rexample.mytasks.data.repository.ITaskRepository
import com.rexample.mytasks.data.repository.IUserRepository
import com.rexample.mytasks.data.repository.TaskRepository
import com.rexample.mytasks.data.repository.UserRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

private val dataStorePreferenceAuth = named("AuthPreference")

val Context.authDataStore by preferencesDataStore(name = "auth_preference")

fun provideAuthDataStore(context: Context): DataStore<Preferences> {
    return context.authDataStore
}

val dataModule = module {
    single<MyTasksDatabase> {
        Room.databaseBuilder(
            androidContext(),
            MyTasksDatabase::class.java,
            "mytasks_database"
        ).fallbackToDestructiveMigration().build()
    }

    single(dataStorePreferenceAuth) { provideAuthDataStore(androidContext()) }
    single<AuthPreference>{
        AuthPreference(get(dataStorePreferenceAuth))
    }

    single<UserDao> { get<MyTasksDatabase>().userDao() }
    single<CategoryDao> { get<MyTasksDatabase>().categoryDao() }
    single<TaskDao> { get<MyTasksDatabase>().taskDao() }

    single<IUserRepository> { UserRepository(get(), get()) }
    single<ICategoryRepository> { CategoryRepository(get()) }
    single<ITaskRepository> { TaskRepository(get()) }
}