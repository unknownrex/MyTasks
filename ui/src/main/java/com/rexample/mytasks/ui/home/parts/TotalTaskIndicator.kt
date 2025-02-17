package com.rexample.mytasks.ui.home.parts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.white
import com.rexample.mytasks.ui.core.util.trimText

@Composable
fun TotalTaskIndicator(text: String?) {
    Box(
        modifier = Modifier
            .background(color = primary, shape = RoundedCornerShape(8.dp))
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text ?: "0",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = white,
            style = trimText()
        )
    }
}

@Preview
@Composable
private fun LabelIconView() {
    MyTasksTheme {
        TotalTaskIndicator(text = "3")
    }
}