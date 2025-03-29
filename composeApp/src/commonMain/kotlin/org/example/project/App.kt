package org.example.project

import androidx.compose.runtime.Composable
import org.example.project.feature.GameVerbViewModel
import org.example.project.feature.GameVisorRoot
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val gameViewModel = GameVerbViewModel()
    GameVisorRoot(gameViewModel = gameViewModel)

}
