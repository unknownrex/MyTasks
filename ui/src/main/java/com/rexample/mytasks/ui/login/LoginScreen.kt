package com.rexample.mytasks.ui.login

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.data.entity.UserEntity
import com.rexample.mytasks.data.mechanism.Resource
import com.rexample.mytasks.ui.R
import com.rexample.mytasks.ui.core.parts.AppButton
import com.rexample.mytasks.ui.core.parts.AppTextField
import com.rexample.mytasks.ui.register.RegisterForm
import com.rexample.mytasks.ui.register.RegisterUiAction
import com.rexample.mytasks.ui.register.RegisterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreen(
    navigate: (dataUser: UserEntity?) -> Unit,
    navigateRegister: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mytasks_icon),
                    contentDescription = null,
                    modifier = Modifier.size(54.dp)
                )
                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LoginForm(navigate = navigate)

            Row {
                Text(text = stringResource(R.string.dont_have_account))
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(id = R.string.register),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .clickable {
                            navigateRegister()
                        }
                )
            }
        }
    }
}

@Composable
fun LoginForm(
    viewModel: LoginViewModel = koinViewModel(),
    navigate: (dataUser: UserEntity?) -> Unit
) {
    val state = viewModel.state.collectAsState()
    val action = { action: LoginUiAction -> viewModel.doAction(action) }
    val context = LocalContext.current

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = stringResource(R.string.login),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppTextField(
                    value = state.value.emailInput,
                    onValueChange = {
                        action(
                            LoginUiAction.UpdateEmail(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.email))
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Filled.Email,
                            contentDescription = null
                        )
                    },
                    isError = state.value.emailInputStatus is Resource.Error || state.value.loginResult is Resource.Error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    supportingText = {
                        if (state.value.emailInputStatus is Resource.Error) Text(
                            text = state.value.emailInputStatus?.message
                                ?: stringResource(R.string.email_password_not_valid),
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 14.sp
                        )
                    }
                )


                val trailingIcon1 = if (state.value.passwordHidden) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                AppTextField(

                    value = state.value.passwordInput,
                    onValueChange = {
                        action(
                            LoginUiAction.UpdatePassword(it)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(text = stringResource(R.string.password))
                    },
                    visualTransformation = if (state.value.passwordHidden) {
                        PasswordVisualTransformation()
                    } else {
                        VisualTransformation.None
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            if (state.value.passwordHidden) action(LoginUiAction.ShowPassword)
                            else action(LoginUiAction.HidePassword)
                        }) {
                            Icon(
                                trailingIcon1,
                                contentDescription = stringResource(R.string.show_password)
                            )
                        }
                    },
                    isError = state.value.passwordInputStatus is Resource.Error || state.value.loginResult is Resource.Error,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    supportingText = {
                        if (state.value.passwordInputStatus is Resource.Error || state.value.loginResult is Resource.Error) {
                            Text(
                                text = state.value.passwordInputStatus?.message
                                    ?: stringResource(R.string.email_or_password_wrong),
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 14.sp
                            )
                        }
                    }
                )

                Spacer(Modifier.height(6.dp))
                AppButton(
                    text = stringResource(id = R.string.login),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.value.emailInput != "" &&
                            state.value.passwordInput != "",
                    onClick = {
                        action(LoginUiAction.LoginUser)
                        if(state.value.loginResult is Resource.Success) {
                            Toast.makeText(context, "Login Berhasil", Toast.LENGTH_SHORT).show()
                            navigate(state.value.loginResult.data)
                        }
                    }
                )
            }
        }
    }
}