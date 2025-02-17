package com.rexample.mytasks.ui.home.parts

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rexample.mytasks.ui.core.theme.MyTasksTheme

@Composable
fun CustomToggleButton(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val borderColor = Color(0xFF879093)
    val backgroundColor = if (isChecked) MaterialTheme.colorScheme.primary else Color.Transparent
    val iconColor = if (isChecked) Color.White else Color.Transparent

    Box(
        modifier = Modifier
            .size(17.dp)
            .clickable { onCheckedChange(!isChecked) }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val borderWidth = 1.dp.toPx() // Ketebalan border
            val radius = (size.minDimension / 2) - (borderWidth / 2)

            drawCircle(
                color = backgroundColor,
                radius = radius
            )

            drawCircle(
                color = borderColor,
                radius = radius,
                style = Stroke(width = borderWidth)
            )
        }

        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = "Check Icon",
            tint = iconColor,
            modifier = Modifier
                .align(Alignment.Center)
                .size(15.dp)
        )
    }
}





@Composable
@Preview
private fun ToggleButtonPreview() {
    MyTasksTheme {
        var isChecked by remember {
            mutableStateOf(false)
        }
        CustomToggleButton(
            isChecked = isChecked
        ) {
            isChecked = it
        }
    }
}