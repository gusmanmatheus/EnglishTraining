package org.example.project.data.local

import kotlinx.coroutines.flow.Flow
import org.example.project.data.dao.VerbDao
import org.example.project.data.dao.VerbEntity
import org.example.project.domain.LocalIrregularVerbsDataSource

class LocalIrregularVerbsDataSourceImpl(private val verbDao: VerbDao): LocalIrregularVerbsDataSource {
    override suspend fun getAllLocalVerbs(): Flow<List<VerbEntity>> {
        return verbDao.getAllVerbsNotDone()
    }

    override suspend fun updateAllLocalVerbs(verbs: List<VerbEntity>): Int {
    return  verbDao.updateList(verbs).first().toInt()
    }

    override suspend fun updateLocalVerb(name: String, isDone: Boolean): Int {
      return verbDao.updateIsDone(name, isDone)
    }

}