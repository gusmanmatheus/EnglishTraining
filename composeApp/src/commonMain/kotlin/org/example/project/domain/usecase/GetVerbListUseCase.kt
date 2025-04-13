package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.Verb

interface GetVerbListUseCase {
    suspend fun invoke(): Flow<List<Verb>>
}