package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.dao.CategoryDao
import com.rexample.mytasks.data.entity.CategoryEntity

class CategoryRepository(private val categoryDao: CategoryDao) : ICategoryRepository {
    override suspend fun insertCategory(category: CategoryEntity) {
        categoryDao.insertCategory(category)
    }

    override suspend fun updateCategory(category: CategoryEntity) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteCategory(category: CategoryEntity) {
        categoryDao.deleteCategory(category)
    }

    override suspend fun getAllCategories(userId: Int): List<CategoryEntity> {
        return categoryDao.getAllCategories(userId)
    }
}