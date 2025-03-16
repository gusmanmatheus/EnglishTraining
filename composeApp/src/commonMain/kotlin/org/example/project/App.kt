package org.example.project

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
 val alphabet = "abcdefghijklmnopqrstuvxyz".toList()
 LetterGrid(alphabet)
}