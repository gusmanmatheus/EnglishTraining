package org.example.project.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.example.project.Verb
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb
import org.example.project.data.verbList

class GameVerbViewModel : ViewModel() {
    private val verbs = verbList
    val verbsLimit: Float = verbs.count().toFloat()

    private val _stateTenseVerb = MutableStateFlow(
        GameState(
            triedAnswer = TriedAnswer(
                listOf(),
                correctAnswer = verbs[0].gerund,
                allLetters = verbs[0].gettingAllLetters()

            ),
            verb = verbs[0],
            progressValue = 0,
        )
    )

    val stateVerb: StateFlow<Verb> = _stateTenseVerb.map { it.verb }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.verb)

    val stateTriedAnswer: StateFlow<TriedAnswer> = _stateTenseVerb.map { it.triedAnswer }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _stateTenseVerb.value.triedAnswer
        )

    val stateListTenseVerb: StateFlow<List<TenseVerb>> = _stateTenseVerb.map { it.listTenseVerb }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _stateTenseVerb.value.listTenseVerb
        )

    val stateProgressValue: StateFlow<Int> = _stateTenseVerb.map { it.progressValue }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _stateTenseVerb.value.progressValue
        )

    val stateFilteredLetters: StateFlow<List<Char>> =
        _stateTenseVerb.map { it.triedAnswer.allLetters }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000),
                _stateTenseVerb.value.triedAnswer.allLetters
            )


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

                listTenseVerb = listUpdated,
                triedAnswer = TriedAnswer(
                    tried = listOf(),
                    correctAnswer = _stateTenseVerb.value.verb.gettingTheSelected(tenseVerb),
                    correctLetters = it.triedAnswer.correctLetters,
                    allLetters = removingUsedList(
                        it.verb.gettingAllLetters(),
                        it.triedAnswer.correctLetters
                    )
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
        _stateTenseVerb.update {
            it.copy(
                triedAnswer = it.triedAnswer.copy(
                    tried = it.triedAnswer.tried + typing,
                    correctLetters = it.triedAnswer.correctLetters,
                    allLetters = removingUsedList(
                        it.verb.gettingAllLetters(),
                        it.triedAnswer.tried + typing + it.triedAnswer.correctLetters
                    )
                ),

                )
        }
    }

    private fun removingLetter(index: Int) {
        val tried = _stateTenseVerb.value.triedAnswer.tried.filterIndexed { i, _ -> i != index }
        _stateTenseVerb.update {
            it.copy(
                triedAnswer = it.triedAnswer.copy(
                    tried = tried,
                    correctLetters = it.triedAnswer.correctLetters,
                    allLetters = removingUsedList(
                        it.verb.gettingAllLetters(),
                        tried + it.triedAnswer.correctLetters
                    )
                ),

                )
        }
    }

    private fun verifyAnswer() {
        _stateTenseVerb.value.let {
            if (it.triedAnswer.tried.joinToString(separator = "") == it.triedAnswer.correctAnswer) {
                val correctLetter = it.triedAnswer.correctLetters + it.triedAnswer.tried
                _stateTenseVerb.update { gameState ->
                    gameState.copy(
                        listTenseVerb = gameState.listTenseVerb.map { item ->
                            if (item.stateAsk == StateAsk.SELECTED) item.copy(
                                stateAsk = StateAsk.CORRECT
                            ) else item
                        },
                        triedAnswer = it.triedAnswer.copy(
                            correctLetters = correctLetter,
                            allLetters = removingUsedList(
                                it.verb.gettingAllLetters(),
                                 correctLetter
                            ),
                        ),
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
        if (_stateTenseVerb.value.progressValue >= verbs.count()) {
            Throwable("ERRO END GAME")
        }

        _stateTenseVerb.value = GameState(
            triedAnswer = TriedAnswer(
                tried = listOf(),
                correctAnswer = verbs[newProgress].gerund,
                correctLetters = listOf(),
                allLetters = verbs[newProgress].gettingAllLetters()
            ),
            verb = verbs[newProgress],
            progressValue = newProgress,
        )
    }


    private fun removingUsedList(allLetters: List<Char>, usedList: List<Char>): List<Char> =
        allLetters.toMutableList().apply {
            usedList.forEach {
                remove(it)
            }
        }
}