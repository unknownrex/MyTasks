package com.rexample.mytasks.ui.core.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.gray
import com.rexample.mytasks.ui.core.theme.white

@Composable
fun ConfirmDeleteDialog(
    title: String,
    text: String,
    onConfirmClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors().copy(
                containerColor = white
            )
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 14.dp, start = 14.dp, end = 14.dp)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = black,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = text,
                    fontWeight = FontWeight.Medium,
                    color = gray,
                    fontSize = 14.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    TextButton(
                        onClick = { onDismissRequest() },
                    ) {
                        Text(
                            text = "TIDAK",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 16.sp,
                        )
                    }
                    TextButton(
                        onClick = { onConfirmClick() },
                    ) {
                        Text(
                            text = "YA",
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 16.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ConfirmDeleteDialogPreview() {
    MyTasksTheme {
        ConfirmDeleteDialog(
            title = "Hapus task?",
            text = "Apakah anda yakin untuk menghapus task ini?",
            onConfirmClick = {},
            onDismissRequest = {}
        )
    }
}