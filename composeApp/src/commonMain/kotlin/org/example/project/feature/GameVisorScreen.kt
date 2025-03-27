package org.example.project.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.map
import org.example.project.Verb
import org.example.project.assets.Black
import org.example.project.assets.ForestGreen
import org.example.project.assets.LimeGreen
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb

@Composable
fun GameVisorRoot(gameViewModel: GameVerbViewModel) {

    val state by gameViewModel.stateTenseVerb.collectAsStateWithLifecycle()

    val verb by gameViewModel.stateVerb.collectAsStateWithLifecycle()
    val tried by gameViewModel.stateTriedAnswer.collectAsStateWithLifecycle()
    val progress by gameViewModel.stateProgressValue.collectAsStateWithLifecycle()
    val filteredLetters by gameViewModel.stateFilteredLetters.collectAsStateWithLifecycle()
    val isButtonEnable by gameViewModel.stateIsButtonEnable.collectAsStateWithLifecycle()
    val listTenseVerb by gameViewModel.stateListTenseVerb.collectAsStateWithLifecycle()


    GameVisorScreen(
        verb,
        tried,
        progress,
        filteredLetters,
         listTenseVerb,
        onAction = { action ->
            gameViewModel.onAction(action)
        })
}

@Composable
fun GameVisorScreen(
    verb: Verb,
    tried: TriedAnswer,
    progress: Int,
    filteredLetters: List<Char>,
     listTenseVerb: List<TenseVerb>,    onAction: (GameScreenAction) -> Unit
) {


    val isButtonEnable by remember(tried) {
        derivedStateOf {
            tried.tried.count() >= tried.correctAnswer.length
        }
    }

    Column {
        Text(
            "${verb.present} / ${verb.portuguese}",
            textAlign = TextAlign.Center,
            color = ForestGreen,
            fontWeight = FontWeight.Bold,
            fontSize = 56.sp,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        )


        Column(modifier = Modifier.fillMaxWidth()) {
            AskVerbIndicatorList(
                listTenseVerb,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                onAction(GameScreenAction.ChangeVerbTense(it))
            }
            TextReceiver(
                tried.tried,
                tried.maxLetter,
                modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(16.dp)

            ) { index -> onAction(GameScreenAction.RemovingLetter(index)) }
        }
        Button(
            onClick = { onAction(GameScreenAction.Answer) },
            modifier = Modifier.padding(16.dp)
                .align(Alignment.End),
            enabled = isButtonEnable,
            colors = ButtonColors(
                contentColor = ForestGreen,
                disabledContainerColor = Color.Gray,
                containerColor = LimeGreen,
                disabledContentColor = Color.DarkGray
            ),
            content = { Text("Answer") }
        )


        LetterGrid(
            verb.filteredLetters,
            onClick = { letter ->
                if (tried.maxLetter > tried.tried.count()) {
                    onAction(GameScreenAction.TypingAnswer(letter))

                }
            })
    }
}


@Composable
fun AskVerbIndicatorList(
    tenseVerbList: List<TenseVerb>,
    modifier: Modifier,
    onClick: (tenseVerb: TenseVerb) -> Unit
) {
    Column(modifier = modifier) {
        LazyRow {
            items(items = tenseVerbList, key = { it.name }) { item ->
                AskVerbIndicatorItem(stateVerb = item, modifier = Modifier, {
                    onClick(item)
                })
            }
        }
    }
}


@Composable
fun AskVerbIndicatorItem(
    stateVerb: TenseVerb,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val (backgroundColor, textColor) = when (stateVerb.stateAsk) {
        StateAsk.NORMAL -> Pair(Color.White, Color.Black)
        StateAsk.CORRECT -> Pair(Color.White, LimeGreen)
        StateAsk.SELECTED -> Pair(LimeGreen, Color.White)
    }


    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = stateVerb.name, color = textColor)
        }
    }
}


@Composable
fun TextReceiver(
    chosenLetters: List<Char>,
    maxLetter: Int,
    modifier: Modifier,
    onClick: (Int) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Text("${chosenLetters.count()} / $maxLetter")
        LazyRow() {
            itemsIndexed(chosenLetters) { index, letter ->
                LetterCompose(letter) {
                    onClick(index)
                }
            }

        }
    }
}

@Composable
fun RangeLinearProgressBar(
    progress: Float,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(10.dp)
            .background(Black, shape = RoundedCornerShape(6.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = (progress) / range.endInclusive)
                .height(10.dp)
                .background(ForestGreen, shape = RoundedCornerShape(5.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress / range.endInclusive)
                .height(10.dp)
                .background(color = LimeGreen, shape = RoundedCornerShape(5.dp))
        )
    }
}
//        val progress by remember { mutableStateOf(1f) }
//        val range = 1f..10f
//
//        RangeLinearProgressBar(
//            progress,
//            range,
//            modifier = Modifier.padding(16.dp)
//        )

//
//        Spacer(Modifier.padding(16.dp))
//        Box(
//            modifier = Modifier.background(color = Color.Green)
//                .fillMaxWidth()
//                .padding(16.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = "Arise/Despertar",
//                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
//                modifier = Modifier
//
//
//            )
//        }}