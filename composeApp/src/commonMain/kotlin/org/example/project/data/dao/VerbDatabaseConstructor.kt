package org.example.project.data.dao

import androidx.room.RoomDatabaseConstructor

@Suppress("NO_ACTUAL_FOR_EXPECT")
 expect object VerbDatabaseConstructor: RoomDatabaseConstructor<VerbDataBase> {
    override fun initialize(): VerbDataBase
}