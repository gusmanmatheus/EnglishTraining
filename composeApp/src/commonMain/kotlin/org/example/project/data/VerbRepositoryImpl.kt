package org.example.project.data

import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.domain.LocalIrregularVerbsDataSource
import org.example.project.domain.Verb
import org.example.project.domain.VerbRepository
import org.example.project.domain.toVerbEntities
import org.example.project.domain.toVerbs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.flowOn

class VerbRepositoryImpl(private val localIrregularVerbsDataSource: LocalIrregularVerbsDataSource) :
    VerbRepository {
    override suspend fun getLocalIrregularVerbs(): Flow<List<Verb>> {
        return localIrregularVerbsDataSource.getAllLocalVerbs().map {
            it.toVerbs()
        }
    }

    override suspend fun getRemoteIrregularVerbs(): Flow<List<Verb>> {
        return flow {
            emit(verbList)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateLocalIrregularVerbs(verbs: List<Verb>): Int {
        return localIrregularVerbsDataSource.updateAllLocalVerbs(verbs.toVerbEntities())
    }

    override suspend fun updateLocalVerb(verb: Verb): Int {
        return localIrregularVerbsDataSource.updateLocalVerb(verb.present, true)
    }
}