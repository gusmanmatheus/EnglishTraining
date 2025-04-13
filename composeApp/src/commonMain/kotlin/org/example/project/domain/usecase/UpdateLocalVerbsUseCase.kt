package org.example.project.domain.usecase

import kotlinx.coroutines.flow.Flow

interface UpdateLocalVerbsUseCase {
     suspend operator fun invoke(): Flow<Boolean>
}