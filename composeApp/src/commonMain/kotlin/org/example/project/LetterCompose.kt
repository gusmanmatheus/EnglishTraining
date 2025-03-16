package org.example.project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LetterCompose(letter: Char) {
     Box(
        modifier = Modifier
            .size(with(LocalDensity.current) {50.dp })
            .padding(4.dp)
            .background(Color.Green, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
    ) {
         Text(
            text = letter.uppercase(),
            fontSize = 35.sp,
            color = Color.White,
        )
    }
}



@Composable
@Preview
fun previewLetterCompose() {
    LetterCompose( 'A')
}