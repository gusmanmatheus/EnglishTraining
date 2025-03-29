package org.example.project.feature

import org.example.project.Verb
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb

data class GameState(
    val verb: Verb = verbInitial,
    val progressValue: Int = 0,
    var listTenseVerb: List<TenseVerb> = initialTenseVerbList,
    val triedAnswer: TriedAnswer
)

private val initialTenseVerbList = listOf(
    TenseVerb.Gerund(StateAsk.SELECTED),
    TenseVerb.PastSimple(StateAsk.NORMAL),
    TenseVerb.ThirdPerson(StateAsk.NORMAL),
    TenseVerb.PastParticiple(StateAsk.NORMAL)
)
val verbInitial = Verb(
    "Despertar",
    "Arise",
    "Arising",
    "Arises",
    "Arose",
    "Arisen"
)

data class TriedAnswer(
    val tried: List<Char>,
    val correctAnswer: String,
    val maxLetter: Int = correctAnswer.count(),
    val correctLetters: List<Char> = listOf(),
    var allLetters: List<Char> = listOf(),
 )


