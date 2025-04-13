package org.example.project.domain.usecase

import org.example.project.domain.Verb

interface SaveIsDoneVerbUseCase {
   suspend  operator fun invoke(verb: Verb)
}