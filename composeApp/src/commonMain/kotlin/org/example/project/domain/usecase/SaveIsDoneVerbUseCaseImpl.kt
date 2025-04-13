package org.example.project.domain.usecase

import org.example.project.domain.Verb
import org.example.project.domain.VerbRepository

class SaveIsDoneVerbUseCaseImpl(private val repository: VerbRepository) : SaveIsDoneVerbUseCase {
    override suspend fun invoke(verb: Verb) {
        repository.updateLocalVerb(verb)
    }

}