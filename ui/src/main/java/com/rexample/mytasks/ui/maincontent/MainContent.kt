package com.rexample.mytasks.ui.maincontent

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.rexample.mytasks.ui.addnewtask.NewTaskScreen
import com.rexample.mytasks.ui.edittask.EditTaskScreen
import com.rexample.mytasks.ui.home.HomeScreen
import com.rexample.mytasks.ui.maincontent.tabbar.TabBarItem
import com.rexample.mytasks.ui.maincontent.tabbar.TabBarView
import com.rexample.mytasks.ui.managecategory.ManageCategoryScreen
import com.rexample.mytasks.ui.profile.ProfileScreen


@Composable
fun MainContent(
    navigateLogin: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    val homeTab = TabBarItem(
        index = 0,
        title = "Home",
        icon = Icons.Filled.Home,
        navigate = { navController.navigate(HomeViewRoute) }
    )

    val profileTab = TabBarItem(
        index = 1,
        title = "Profile",
        icon = Icons.Filled.Person,
        navigate = { navController.navigate(ProfileRoute) }
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    val showBottomBar by remember {
        derivedStateOf {
            currentBackStackEntry?.destination?.hierarchy?.any {
                it.hasRoute(HomeViewRoute::class) || it.hasRoute(ProfileRoute::class)
            } ?: false
        }
    }

    var selectedTab by remember{ mutableStateOf(0) }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TabBarView(
                    selectedIndex = selectedTab,
                    selectIndex = {selectedTab = it},
                    fabItemNavigate = { navController.navigate(AddTaskRoute) },
                    profileTab = profileTab,
                    homeTab = homeTab
                )
            }
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = HomeViewRoute) {
            composable<HomeViewRoute> {
                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    onEditTask = { taskId ->
                        navController.navigate(
                            EditTaskRoute(
                                taskId
                            )
                        )
                    },
                    navigateCategory = {
                        navController.navigate(ManageCategoryRoute)
                    }
                )
            }

            composable<AddTaskRoute> {
                NewTaskScreen(
                    navigateBack = { navController.navigateUp() }
                )
            }

            composable<EditTaskRoute> { backStackEntry ->
                val taskId = backStackEntry.toRoute<EditTaskRoute>().taskId
                EditTaskScreen(taskId = taskId, navigateBack = { navController.navigateUp() })
            }

            composable<ProfileRoute> {
                ProfileScreen(
                    modifier = Modifier.padding(innerPadding),
                    navigateLogin = {navigateLogin()}
                )
            }

            composable<ManageCategoryRoute> {
                ManageCategoryScreen(navigateBack = { navController.navigateUp() })
            }
        }
    }
}