package org.example.project.domain


sealed class TenseVerb(open val stateAsk: StateAsk, open val name: String) {

    abstract fun copy(stateAsk: StateAsk): TenseVerb

    data class Gerund(override val stateAsk: StateAsk, override val name: String = "Gerund") :
        TenseVerb(stateAsk, name) {
        override fun copy(stateAsk: StateAsk) = Gerund(stateAsk)
    }

    data class ThirdPerson(override val stateAsk: StateAsk, override val name: String = "Third Person") :
        TenseVerb(stateAsk, name) {
        override fun copy(stateAsk: StateAsk) = ThirdPerson(stateAsk)
    }

    data class PastSimple(override val stateAsk: StateAsk, override val name: String = "Past Simple") :
        TenseVerb(stateAsk, name) {
        override fun copy(stateAsk: StateAsk) = PastSimple(stateAsk)
    }

    data class PastParticiple(override val stateAsk: StateAsk, override val name: String = "Past Participle") :
        TenseVerb(stateAsk, name) {
        override fun copy(stateAsk: StateAsk) = PastParticiple(stateAsk)
    }
}

enum class StateAsk {
    CORRECT,
    SELECTED,
    NORMAL
}