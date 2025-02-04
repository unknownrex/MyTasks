package com.rexample.mytasks.ui.landingpage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import kotlinx.coroutines.delay

@Composable
fun MySplashScreen(
    modifier: Modifier = Modifier,
    navigate: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        delay(2000)
        navigate()
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.mytasks_icon),
            contentDescription = null,
            modifier = Modifier.size(150.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MySplashScreenPreview() {
    MyTasksTheme {
        MySplashScreen(
            navigate = {}
        )
    }
}