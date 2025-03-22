package org.example.project.feature

import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb

data class GameState(
    val verb: String = "",
    val progressValue: Int = 0,
    var listTenseVerb:List<TenseVerb> = initialTenseVerbList
)
private val initialTenseVerbList = listOf(
    TenseVerb.Gerund(StateAsk.SELECTED),
    TenseVerb.PastSimple(StateAsk.NORMAL),
    TenseVerb.ThirdPerson(StateAsk.NORMAL),
    TenseVerb.PastParticiple(StateAsk.NORMAL)
)