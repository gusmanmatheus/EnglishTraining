package org.example.project.domain

import kotlinx.coroutines.flow.Flow
import org.example.project.data.dao.VerbEntity

interface LocalIrregularVerbsDataSource {
    suspend fun getAllLocalVerbs(): Flow<List<VerbEntity>>
    suspend fun updateAllLocalVerbs(verbs: List<VerbEntity>) : Int
    suspend fun updateLocalVerb(name: String, isDone: Boolean): Int
}