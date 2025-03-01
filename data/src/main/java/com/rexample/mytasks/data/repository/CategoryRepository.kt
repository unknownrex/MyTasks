package com.rexample.mytasks.data.repository

import com.rexample.mytasks.data.dao.CategoryDao
import com.rexample.mytasks.data.entity.CategoryEntity
import com.rexample.mytasks.data.mechanism.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepository(private val categoryDao: CategoryDao) : ICategoryRepository {
    override fun insertCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.insertCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menambahkan kategori: ${e.message}"))
        }
    }

    override fun updateCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.updateCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal memperbarui kategori: ${e.message}"))
        }
    }

    override fun deleteCategory(category: CategoryEntity): Flow<Resource<Unit>> = flow {
        try {
            emit(Resource.Loading())
            categoryDao.deleteCategory(category)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal menghapus kategori: ${e.message}"))
        }
    }

    override fun getAllCategories(): Flow<Resource<List<CategoryEntity>>> = flow {
        try {
            emit(Resource.Loading())
            val result = categoryDao.getAllCategories()
            emit(Resource.Success(result))
        } catch (e: Exception) {
            emit(Resource.Error("Gagal mengambil kategori: ${e.message}"))
        }
    }
}
