package org.example.project.data.dao

import androidx.room.RoomDatabase

expect class DatabaseFactory {
    fun create(): RoomDatabase.Builder<VerbDataBase>
}