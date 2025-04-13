package org.example.project

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object IrregularVerbs: Route
}