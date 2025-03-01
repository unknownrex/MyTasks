package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow

interface ICategoryRepository {
    fun insertCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun updateCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun deleteCategory(category: CategoryEntity): Flow<Resource<Unit>>
    fun getAllCategories(): Flow<Resource<List<CategoryEntity>>>
}