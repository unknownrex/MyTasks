package com.rexample.mytasks

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.home.HomeScreen
import com.rexample.mytasks.ui.landingpage.MySplashScreen
import com.rexample.mytasks.ui.login.LoginScreen
import com.rexample.mytasks.ui.maincontent.MainContent
import com.rexample.mytasks.ui.nav.HomeScreenRoute
import com.rexample.mytasks.ui.nav.LoginRoute
import com.rexample.mytasks.ui.nav.RegisterRoute
import com.rexample.mytasks.ui.nav.SplashScreenRoute
import com.rexample.mytasks.ui.register.RegisterScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AppContainer(
    viewModel: AppViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    val state = viewModel.state.collectAsState()

    var splashscreenHasShown by rememberSaveable {
        mutableStateOf(false)
    }
    var homeRouteHasShown by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    
    LaunchedEffect(key1 = lifecycleState) {
        if (lifecycleState == Lifecycle.State.RESUMED && splashscreenHasShown){
            viewModel.doAction(AppUiAction.CheckSession)
        }
    }

    LaunchedEffect(key1 = state.value.isSessionValid) {
        Log.d("AppContainer", "Session valid: ${state.value.isSessionValid}")
        if(state.value.isSessionValid == true && !homeRouteHasShown){
            navController.navigate(HomeScreenRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
            homeRouteHasShown = true
        }else if(state.value.isSessionValid == false){
            navController.navigate(LoginRoute){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
            homeRouteHasShown = false
        }
    }

    MyTasksTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavHost(navController = navController, startDestination = SplashScreenRoute) {
                composable<SplashScreenRoute> {
                    MySplashScreen(
                        navigate = {
                            viewModel.doAction(AppUiAction.CheckSession)
                            splashscreenHasShown = true
                        }
                    )
                }
                
                composable<LoginRoute> {
                    LoginScreen(
                        navigate = {
                            navController.navigate(HomeScreenRoute) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        },
                        navigateRegister = {
                            navController.navigate(RegisterRoute)
                        }
                    )
                }

                composable<RegisterRoute> {
                    RegisterScreen(
                        navigateLogin = {
                            navController.navigate(LoginRoute)
                        }
                    )
                }

                composable<HomeScreenRoute> {
                    MainContent(
                        navigateLogin = {
                            navController.navigate(LoginRoute) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}