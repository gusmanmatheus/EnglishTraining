package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.Verb
import org.example.project.domain.VerbRepository

class GetVerbListUseCaseImpl(private val repository: VerbRepository): GetVerbListUseCase {
    override suspend fun invoke(): Flow<List<Verb>> {
        return repository.getLocalIrregularVerbs()
    }
}