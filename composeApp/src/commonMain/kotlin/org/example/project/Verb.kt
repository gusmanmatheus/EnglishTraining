package org.example.project

import org.example.project.data.TenseVerb

data class Verb(
    val portuguese: String,
    val present: String,
    val gerund: String,
    val thirdPerson: String,
    val simplePast: String,
    val pastParticiple: String,
    val lettersUsed: List<Char> = listOf(),
    var filteredLetters: List<Char> = emptyList(),
    var answered: List<String> = emptyList()
) {
    init {
        filteredLetters = filterLettersUsed()
    }

    private fun filterLettersUsed(): List<Char> {
        return gettingAllLettersNotAnswered().toMutableList().apply {
            lettersUsed.forEach { remove(it) }
        }
    }

    fun gettingAllLettersNotAnswered(): List<Char> {
        return (gerund + thirdPerson + simplePast + pastParticiple)
            .toMutableList().apply {
                answered.forEach {
                    it.toList().forEach {item ->
                        this.remove(item)
                    }
                }
            }.sorted()
    }

    fun gettingTheSelected(verbTense: TenseVerb) =
        when (verbTense) {
            is TenseVerb.Gerund -> this.gerund
            is TenseVerb.ThirdPerson -> this.thirdPerson
            is TenseVerb.PastSimple -> this.simplePast
            is TenseVerb.PastParticiple -> pastParticiple
        }
}
