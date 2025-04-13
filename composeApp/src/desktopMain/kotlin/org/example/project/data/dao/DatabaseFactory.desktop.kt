package org.example.project.data.dao

import androidx.room.RoomDatabase
import java.io.File
import androidx.room.Room

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<DatabaseFactory> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bookpedia")
            os.contains("mac") -> File(userHome, "Library/Application Support/Bookpedia")
            else -> File(userHome, ".local/share/Bookpedia")
        }

        if(!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, VerbDataBase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}