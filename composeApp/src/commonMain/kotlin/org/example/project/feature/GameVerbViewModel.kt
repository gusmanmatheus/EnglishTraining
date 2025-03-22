package org.example.project.feature

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb

class GameVerbViewModel : ViewModel() {
    val stateFlowList = MutableStateFlow(GameState())


    private val _stateTenseVerb = MutableStateFlow(
        GameState()
        )

    val stateTenseVerb: StateFlow<GameState> = _stateTenseVerb


    private fun changeSelected(tenseVerb: TenseVerb) {
        val isNotNormal =
            _stateTenseVerb.value.listTenseVerb.count { it.name == tenseVerb.name && it.stateAsk != StateAsk.NORMAL } >= 1
        if(isNotNormal) return

       val listUpdated = _stateTenseVerb.value.listTenseVerb.map { currentVerb ->
            when {
                currentVerb.stateAsk == StateAsk.SELECTED-> currentVerb.copy(stateAsk = StateAsk.NORMAL)
                currentVerb == tenseVerb -> tenseVerb.copy(stateAsk = StateAsk.SELECTED)
                else -> currentVerb
            }
        }
        _stateTenseVerb.update {
            it.copy(
                listTenseVerb  =  listUpdated
            )
        }
    }

    fun onAction(gameScreenAction: GameScreenAction){
        when(gameScreenAction){
            is GameScreenAction.ChangeVerbTense ->{
               changeSelected(gameScreenAction.verbTense)
            }
            else ->{}
        }
    }
}