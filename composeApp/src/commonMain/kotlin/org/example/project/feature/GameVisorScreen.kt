package org.example.project.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.assets.Black
import org.example.project.assets.ForestGreen
import org.example.project.assets.LimeGreen
import org.example.project.data.StateAsk
import org.example.project.data.TenseVerb

@Composable
fun GameVisorRoot(gameViewModel: GameVerbViewModel) {

    val tenseVerbList by gameViewModel.stateTenseVerb.collectAsStateWithLifecycle()

    GameVisorScreen(state = tenseVerbList, onAction = { action ->
        gameViewModel.onAction(action)
    })
}

@Composable
fun GameVisorScreen(
    state: GameState,
    onAction: (GameScreenAction) -> Unit
) {
    Row {
        AskVerbIndicatorList(state.listTenseVerb) {
            onAction(GameScreenAction.ChangeVerbTense(it))

        }
    }
}

@Composable
fun AskVerbIndicatorList(tenseVerbList: List<TenseVerb>, onClick: (tenseVerb: TenseVerb) -> Unit) {
    Column {
        LazyColumn {
            items(items = tenseVerbList) { item ->
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
                .fillMaxWidth(fraction = (progress) / range.endInclusive) // Define o final do intervalo
                .height(10.dp)
                .background(ForestGreen, shape = RoundedCornerShape(5.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = progress / range.endInclusive) // Define a barra de progresso
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