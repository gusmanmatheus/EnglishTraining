package org.example.project.domain

import kotlinx.coroutines.flow.Flow

interface VerbRepository {
    suspend fun getLocalIrregularVerbs(): Flow<List<Verb>>
    suspend fun getRemoteIrregularVerbs(): Flow<List<Verb>>
    suspend fun updateLocalIrregularVerbs(verbs: List<Verb>): Int
    suspend fun updateLocalVerb(verb: Verb): Int
}