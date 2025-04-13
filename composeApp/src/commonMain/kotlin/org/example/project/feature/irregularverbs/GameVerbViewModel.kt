package org.example.project.feature.irregularverbs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.project.data.verbList
import org.example.project.domain.StateAsk
import org.example.project.domain.TenseVerb
import org.example.project.domain.Verb
import org.example.project.domain.usecase.GetVerbListUseCase
import org.example.project.domain.usecase.SaveIsDoneVerbUseCase
import org.example.project.domain.usecase.UpdateLocalVerbsUseCase

class GameVerbViewModel(
    private val getVerbListUseCase: GetVerbListUseCase,
    private val saveIsDoneVerbUseCase: SaveIsDoneVerbUseCase,
    private val updateLocalVerbsUseCase: UpdateLocalVerbsUseCase
) : ViewModel() {
    val verbsLimit: Float = verbList.count().toFloat()
    private var verbs: List<Verb> = listOf()

    private val _stateTenseVerb = MutableStateFlow(
        GameState(
            triedAnswer = TriedAnswer(
                listOf(),
                correctAnswer = "Gerund",
                allLetters = listOf()

            ),
            progressValue = -1,
            isLoading = false
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
    val stateIsLoading: StateFlow<Boolean> = _stateTenseVerb.map {
        it.isLoading
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _stateTenseVerb.value.isLoading)

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

    fun updateAllIrregularVerbFromServer() {
        _stateTenseVerb.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            updateLocalVerbsUseCase.invoke().collect { isChanged ->
                     getAllVerbs()

            }
        }
    }

    private fun getAllVerbs() {
        viewModelScope.launch {
            getVerbListUseCase.invoke()
                .distinctUntilChanged()
                .collectLatest {
                    verbs = it
                    nextVerb()

                }
        }
    }

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
                    saveVerbAsDone(verbs[stateProgressValue.value])
                    nextVerb()
                }
            }
        }
    }
    private fun saveVerbAsDone(verb: Verb){
        updateVerb(verb)

    }

    private fun nextVerb() {
        val oldProgress = _stateTenseVerb.value.progressValue
        val newProgress = oldProgress + 1
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
            isLoading = false
        )

    }

    private fun updateVerb(verb: Verb) {
        viewModelScope.launch {
            saveIsDoneVerbUseCase.invoke(verb)
        }
    }

    private fun removingUsedList(allLetters: List<Char>, usedList: List<Char>): List<Char> =
        allLetters.toMutableList().apply {
            usedList.forEach {
                remove(it)
            }
        }
}