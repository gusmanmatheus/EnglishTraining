package org.example.project.feature

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp




@Composable
fun IndicatorProgress(
    indicatorColor: Color,
    foregroundIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    indicatorThickness: Dp = 8.dp,
    animationDuration: Int = 1000,
    dataUsage: Float = 60f,
    displayText: String,
    dataTextStyle: TextStyle = TextStyle(fontSize = 12.sp)
) {
    var dataUsageRemember by remember { mutableFloatStateOf(-1f) }
    LaunchedEffect(Unit) { dataUsageRemember = dataUsage }

    val dataUsageAnimate = animateFloatAsState(
        targetValue = dataUsageRemember,
        animationSpec = tween(durationMillis = animationDuration), label = "animation"
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = indicatorThickness),
        contentAlignment = Alignment.Center
    ) {
        val responsiveSize = (maxWidth.coerceAtMost(maxHeight) * 0.6f).coerceAtMost(150.dp)

        Canvas(modifier = Modifier.size(responsiveSize)) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(indicatorColor, Color.White),
                    center = center,
                    radius = size.minDimension / 2
                ),
                radius = size.minDimension / 2,
                center = center
            )

            drawCircle(
                color = Color.White,
                radius = (responsiveSize / 2 - indicatorThickness).toPx(),
                center = center
            )

            val sweepAngle = dataUsageAnimate.value * 360 / 100
            drawArc(
                color = foregroundIndicatorColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                style = Stroke(width = indicatorThickness.toPx(), cap = StrokeCap.Round),
                size = Size(
                    width = (responsiveSize - indicatorThickness).toPx(),
                    height = (responsiveSize - indicatorThickness).toPx()
                ),
                topLeft = Offset(
                    x = (indicatorThickness / 2).toPx(),
                    y = (indicatorThickness / 2).toPx()
                )
            )
        }
        DisplayText(
            name = displayText,
        animateNumber = dataUsageAnimate,
        dataTextStyle = dataTextStyle,
        )
    }


    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
private fun DisplayText(
    name: String,
    animateNumber: State<Float>,
    dataTextStyle: TextStyle,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {

        Text(
            text = name,
            fontWeight = FontWeight.Bold,
            style = dataTextStyle,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = (animateNumber.value).toInt().toString() + "%",
            style = dataTextStyle
        )
    }
}