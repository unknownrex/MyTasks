package com.rexample.mytasks.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["user_id"]), Index(value = ["category_id"])]
)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "is_done") val isDone: Boolean = false,
    @ColumnInfo(name = "is_pinned") var isPinned: Boolean = false,
    @ColumnInfo(name = "category_id") val categoryId: Int?,
    @ColumnInfo(name = "user_id") val userId: Int?
)
