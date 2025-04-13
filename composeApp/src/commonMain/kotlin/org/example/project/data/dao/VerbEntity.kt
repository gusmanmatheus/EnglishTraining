package org.example.project.data.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verbs")
data class VerbEntity(
    @PrimaryKey val portuguese: String,
    val present: String,
    val gerund: String,
    val thirdPerson: String,
    val simplePast: String,
    val pastParticiple: String,
    val isDone: Boolean

)