package com.rexample.mytasks.ui.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.parts.AppButton
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.black
import com.rexample.mytasks.ui.core.theme.gray
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.secondary
import com.rexample.mytasks.ui.core.theme.white
import com.rexample.mytasks.ui.home.HomeUiAction
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navigateLogin: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val action = { action: ProfileUiAction -> viewModel.doAction(action) }

    val userData = state.userData.data

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        action(ProfileUiAction.GetAllTasks)
        action(ProfileUiAction.GetUserData)
    }

    LaunchedEffect(state.logoutResult) {
        if(state.logoutResult is Resource.Success) {
            Toast.makeText(context, "Logout Berhasil", Toast.LENGTH_SHORT).show()
            navigateLogin()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "MyTasks",
                        fontWeight = FontWeight.Bold,
                        color = primary,
                        fontSize = 24.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = secondary
                ),
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.default_profile),
                        contentDescription = null,
                        modifier = Modifier
                            .size(70.dp)
                            .border(4.dp, secondary, CircleShape)
                            .shadow(2.dp, CircleShape)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column {
                        if (userData != null) {
                            Text(
                                text = userData.username,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = black
                            )

                            Text(
                                text = userData.email,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = gray
                            )
                        }
                    }
                }
                Spacer(Modifier.height(32.dp))
                Text(
                    text = "Ringkasan Tugas",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = black
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskResumeCard(
                        textNumber = state.taskSummary?.totalTasks.toString(),
                        text = "Total Tugas",
                        modifier = Modifier.weight(1f)
                    )
                    TaskResumeCard(
                        textNumber = state.taskSummary?.completedTasks.toString(),
                        text = "Tugas Selesai",
                        modifier = Modifier.weight(1f)
                    )
                    TaskResumeCard(
                        textNumber = state.taskSummary?.overdueTasks.toString(),
                        text = "Tugas Terlambat",
                        modifier = Modifier.weight(1f)
                    )
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = secondary),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Rasio penyelesaian tugas",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = gray
                        )
                        Text(
                            text = "${ state.taskSummary?.completionRate.toString()}%",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = black
                        )
                    }
                }
            }
            AppButton(
                text = "Logout",
                onClick = { action(ProfileUiAction.Logout) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun TaskResumeCard(
    textNumber: String = "0",
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(vertical = 8.dp),
        colors = CardDefaults.cardColors().copy(
            containerColor = secondary
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = textNumber,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                color = black
            )
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                fontSize = 10.sp,
                color = gray
            )
        }
    }
}
/*

@Composable
@Preview
private fun ProfileScreenPreview() {
    MyTasksTheme {
        ProfileScreen()
    }
}*/
