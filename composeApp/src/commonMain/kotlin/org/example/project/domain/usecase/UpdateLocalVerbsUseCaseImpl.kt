package org.example.project.domain.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import org.example.project.domain.VerbRepository

class UpdateLocalVerbsUseCaseImpl(private val repository: VerbRepository) :
    UpdateLocalVerbsUseCase {
    override suspend operator fun invoke() : Flow<Boolean> = flow{
        val remote = repository.getRemoteIrregularVerbs().first()
        val local = repository.getLocalIrregularVerbs().first()
        val localSet = local.map { it.present }.toHashSet()
        val updatedList = remote.filterNot { it.present in localSet }
      emit(  repository.updateLocalIrregularVerbs(local + updatedList) > 0)
    }.flowOn(Dispatchers.IO)
}