package com.reringuy.support.models.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.reringuy.support.models.enums.SynchronizeStatus
import com.reringuy.support.models.enums.TaskStatus

@Entity(
    tableName = "tasks",
    indices = [Index(value = ["id"], unique = true)]
)
data class Task (
    @PrimaryKey
    val uid: Int,
    val id: Int?,
    val title: String,
    val description: String,
    val status: TaskStatus = TaskStatus.PENDING,
    val synced: SynchronizeStatus = SynchronizeStatus.NOT_SYNCHRONIZED,
    val assignedTo: Int,
    val createdBy: Int,
    val createdAt: Long,
    val updatedAt: Long,
)