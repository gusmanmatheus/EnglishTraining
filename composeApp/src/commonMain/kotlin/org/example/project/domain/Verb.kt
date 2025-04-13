package org.example.project.domain

import org.example.project.data.dao.VerbEntity

data class Verb(
     val portuguese: String,
     val present: String,
     val gerund: String,
     val thirdPerson: String,
     val simplePast: String,
     val pastParticiple: String,
     val isDone: Boolean = false
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


fun VerbEntity.toVerb() =
    Verb(
        portuguese = this.portuguese,
        present = this.present,
        gerund = this.gerund,
        thirdPerson = this.thirdPerson,
        simplePast = this.simplePast,
        pastParticiple = this.pastParticiple,
        isDone = this.isDone
    )

fun Verb.toVerbEntity() =
    VerbEntity(
        present = this.present,
        portuguese = this.portuguese,
        gerund = this.gerund,
        thirdPerson = this.thirdPerson,
        simplePast = this.simplePast,
        pastParticiple = this.pastParticiple,
        isDone = this.isDone
    )

fun List<VerbEntity>.toVerbs(): List<Verb> = this.map { it.toVerb() }

fun List<Verb>.toVerbEntities(): List<VerbEntity> = this.map { it.toVerbEntity() }
