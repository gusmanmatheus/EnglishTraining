package org.example.project.feature

import org.example.project.data.TenseVerb


sealed interface GameScreenAction {
    data class ChangeVerbTense(val verbTense: TenseVerb): GameScreenAction
    data object Answer: GameScreenAction
    data object ChangeProgressBar: GameScreenAction
    data object IncorrectTried: GameScreenAction
    data class TypingAnswer(val typing: Char): GameScreenAction
    data class RemovingLetter(val position: Int):GameScreenAction

}
