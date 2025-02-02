package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.entity.CategoryEntity

interface ICategoryRepository {
    suspend fun insertCategory(category: CategoryEntity)
    suspend fun updateCategory(category: CategoryEntity)
    suspend fun deleteCategory(category: CategoryEntity)
    suspend fun getAllCategories(userId: Int): List<CategoryEntity>
}