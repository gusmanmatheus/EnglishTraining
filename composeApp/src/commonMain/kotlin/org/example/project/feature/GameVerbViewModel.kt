package org.example.project.feature

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.Verb
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb
import org.example.project.data.verbList

class GameVerbViewModel : ViewModel() {
    private val verbs = verbList


    private val _stateTenseVerb = MutableStateFlow(
        GameState(
            triedAnswer = TriedAnswer(
                listOf(),
                correctAnswer = verbs[0].gerund,

            ),
            verb = verbs[0],
            progressValue = 0,
         )
    )

    val stateTenseVerb: StateFlow<GameState> = _stateTenseVerb.asStateFlow()

    val stateVerb: StateFlow<Verb> = _stateTenseVerb.map { it.verb }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.verb)

    val stateTriedAnswer: StateFlow<TriedAnswer> = _stateTenseVerb.map { it.triedAnswer }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.triedAnswer)

    val stateListTenseVerb: StateFlow<List<TenseVerb>> = _stateTenseVerb.map { it.listTenseVerb }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.listTenseVerb)

    val stateProgressValue: StateFlow<Int> = _stateTenseVerb.map { it.progressValue }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.progressValue)

    val stateIsButtonEnable: StateFlow<Boolean> = _stateTenseVerb.map {
        it.triedAnswer.tried.count() >= it.triedAnswer.correctAnswer.length
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val stateFilteredLetters: StateFlow<List<Char>> = _stateTenseVerb.map { it.verb.filteredLetters }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.verb.filteredLetters)


    private fun changeSelected(tenseVerb: TenseVerb) {
        val isNotNormal =
            _stateTenseVerb.value.listTenseVerb.count { it.name == tenseVerb.name && it.stateAsk != StateAsk.NORMAL } >= 1
        if (isNotNormal) return

        val listUpdated = _stateTenseVerb.value.listTenseVerb.map { currentVerb ->
            when {
                currentVerb.stateAsk == StateAsk.SELECTED -> currentVerb.copy(stateAsk = StateAsk.NORMAL)
                currentVerb == tenseVerb -> tenseVerb.copy(stateAsk = StateAsk.SELECTED)
                else -> currentVerb
            }
        }
        _stateTenseVerb.update {
            it.copy(
                verb = it.verb.copy(
                    filteredLetters = it.verb.gettingAllLettersNotAnswered(),
                    lettersUsed = listOf()
                ),
                listTenseVerb = listUpdated,
                triedAnswer = TriedAnswer(
                    tried = listOf(),
                    correctAnswer = _stateTenseVerb.value.verb.gettingTheSelected(tenseVerb)
                )
            )
        }
    }

    fun onAction(gameScreenAction: GameScreenAction) {
        when (gameScreenAction) {
            is GameScreenAction.ChangeVerbTense -> {
                changeSelected(gameScreenAction.verbTense)
            }

            is GameScreenAction.TypingAnswer -> {
                changeTypingAnswer(gameScreenAction.typing)
            }

            is GameScreenAction.RemovingLetter -> {
                removingLetter(gameScreenAction.position)
            }

            is GameScreenAction.Answer -> {
                verifyAnswer()
            }

            else -> {}
        }
    }

    private fun changeTypingAnswer(typing: Char) {
        val tried = _stateTenseVerb.value.triedAnswer.tried + typing
        _stateTenseVerb.update {
            it.copy(
                triedAnswer = it.triedAnswer.copy(tried = it.triedAnswer.tried + typing),
                verb = it.verb.copy(lettersUsed = tried)
            )
        }
    }

    private fun removingLetter(index: Int) {
        val tried = _stateTenseVerb.value.triedAnswer.tried.filterIndexed { i, _ -> i != index }
        _stateTenseVerb.update {
            it.copy(
                triedAnswer = it.triedAnswer.copy(
                    tried = tried

                ),
                verb = it.verb.copy(
                    lettersUsed = tried,
                )
            )
        }
    }

    private fun verifyAnswer() {
        _stateTenseVerb.value.let {
            if (it.triedAnswer.tried.joinToString(separator = "") == it.triedAnswer.correctAnswer) {
                _stateTenseVerb.update { gameState ->
                    val actualAnswer =
                        gameState.triedAnswer.correctAnswer
                    gameState.copy(
                        listTenseVerb = gameState.listTenseVerb.map { item ->
                            if (item.stateAsk == StateAsk.SELECTED) item.copy(
                                stateAsk = StateAsk.CORRECT
                            ) else item
                        },
                        verb = it.verb.copy(answered = it.verb.answered + actualAnswer)
                    )
                }
                val nextStatus =
                    it.listTenseVerb.indexOfFirst { item -> item.stateAsk == StateAsk.NORMAL }
                if (nextStatus > -1) {
                    changeSelected(it.listTenseVerb[nextStatus])
                } else {
                    nextVerb()
                }
            }
        }
    }

    private fun nextVerb() {
        val newProgress = _stateTenseVerb.value.progressValue + 1
        if (_stateTenseVerb.value.progressValue >= verbs.count()){
            Throwable("ERRO END GAME")
        }

        _stateTenseVerb.value = GameState(
            triedAnswer = TriedAnswer(
                listOf(),
                correctAnswer = verbs[newProgress].gerund
            ),
            verb = verbs[newProgress],
            progressValue = newProgress
        )
    }
}