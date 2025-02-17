package com.rexample.mytasks.ui.core.appstate.error

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.gray

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    errorMessage: String?,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            modifier = Modifier
                .width(250.dp)
                .height(150.dp),
            painter = painterResource(id = R.drawable.error_state),
            contentDescription = stringResource(id = R.string.something_wrong),
            contentScale = ContentScale.FillWidth
        )
        Text(
            text = stringResource(id = R.string.something_wrong),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error,
            fontSize = 24.sp
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = errorMessage ?: stringResource(R.string.unknown_error),
            fontWeight = FontWeight.Medium,
            maxLines = 5,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis,
            color = gray,
            fontSize = 16.sp
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ErrorView_Preview() {
    MyTasksTheme {
        ErrorView(
            errorMessage = "org.jetbrains.kotlin.gradle.tasks.CompilationErrorException: Compilation error."
        )
    }
}