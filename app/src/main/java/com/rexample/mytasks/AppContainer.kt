package com.rexample.mytasks

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.landingpage.MySplashScreen
import com.rexample.mytasks.ui.maincontent.MainContent
import com.rexample.mytasks.ui.nav.HomeScreenRoute
import com.rexample.mytasks.ui.nav.SplashScreenRoute

@Composable
fun AppContainer() {
    val navController = rememberNavController()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()


    MyTasksTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
        ) {
            NavHost(navController = navController, startDestination = SplashScreenRoute) {
                composable<SplashScreenRoute> {
                    MySplashScreen(
                        navigate = {
                            navController.navigate(HomeScreenRoute) {
                                popUpTo(navController.graph.id) {
                                    inclusive = true
                                }
                            }
                        }
                    )
                }

                composable<HomeScreenRoute> {
                    MainContent()
                }
            }
        }
    }
}