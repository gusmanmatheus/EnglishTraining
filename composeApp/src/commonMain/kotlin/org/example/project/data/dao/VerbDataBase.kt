package org.example.project.data.dao

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [VerbEntity::class],
    version = 1
)

@TypeConverters(
    StringListTypeConverter::class
)
@ConstructedBy(VerbDatabaseConstructor::class)
abstract class VerbDataBase : RoomDatabase() {
    abstract val verbDao: VerbDao

    companion object {
        const val DB_NAME = "Verb.db"
    }
}
