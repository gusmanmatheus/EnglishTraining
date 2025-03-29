package org.example.project

import org.example.project.data.TenseVerb

data class Verb(
    val portuguese: String,
    val present: String,
    val gerund: String,
    val thirdPerson: String,
    val simplePast: String,
    val pastParticiple: String,
) {

    fun gettingAllLetters(): List<Char> {
        return (gerund + thirdPerson + simplePast + pastParticiple).toList().sorted()
    }

    fun gettingTheSelected(verbTense: TenseVerb) =
        when (verbTense) {
            is TenseVerb.Gerund -> this.gerund
            is TenseVerb.ThirdPerson -> this.thirdPerson
            is TenseVerb.PastSimple -> this.simplePast
            is TenseVerb.PastParticiple -> pastParticiple
        }
}
