package com.rexample.mytasks.ui.maincontent.tabbar

import android.app.ActionBar.Tab
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rexample.mytasks.ui.core.theme.MyTasksTheme
import com.rexample.mytasks.ui.core.theme.gray
import com.rexample.mytasks.ui.core.theme.primary
import com.rexample.mytasks.ui.core.theme.white

data class TabBarItem(
    val index: Int,
    val title: String,
    val icon: ImageVector,
    val navigate: () -> Unit = {}
)

@Composable
fun TabBarView(
    selectedIndex: Int,
    selectIndex: (Int) -> Unit,
    fabItemNavigate: () -> Unit,
    profileTab: TabBarItem,
    homeTab: TabBarItem
) {

    Surface(shadowElevation = 10.dp) {
        BottomAppBar(
            modifier = Modifier.height(70.dp),
            tonalElevation = 200.dp,
            containerColor = MaterialTheme.colorScheme.secondary,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                NavigationBarItem(
                    selected = selectedIndex == homeTab.index,
                    onClick = {
                        selectIndex(homeTab.index)
                        homeTab.navigate()
                    },
                    icon = { TabBarIconView(tabBar = homeTab) },
                    label = {
                        Text(
                            text = homeTab.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.75F),
                        selectedIconColor = white,
                        selectedTextColor = primary,
                        unselectedIconColor = gray,
                        unselectedTextColor = gray
                    )
                )

                // FAB di Tengah
                Box(
                    modifier = Modifier.size(50.dp),
                    contentAlignment = Alignment.Center
                ) {
                    FloatingActionButton(
                        onClick = fabItemNavigate,
                        shape = CircleShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        elevation = FloatingActionButtonDefaults.elevation(1.dp),
                        modifier = Modifier.size(80.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                NavigationBarItem(
                    selected = selectedIndex == profileTab.index,
                    onClick = {
                        selectIndex(profileTab.index)
                        profileTab.navigate()
                    },
                    icon = { TabBarIconView(tabBar = profileTab) },
                    label = {
                        Text(
                            text = profileTab.title,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                        )
                    },
                    colors = NavigationBarItemDefaults.colors().copy(
                        selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.75F),
                        selectedIconColor = white,
                        selectedTextColor = primary,
                        unselectedIconColor = gray,
                        unselectedTextColor = gray
                    )
                )
            }
        }
    }
}


@Composable
fun TabBarIconView(
    tabBar: TabBarItem,
) {
    Icon(
        imageVector = tabBar.icon,
        contentDescription = tabBar.title,
    )
}


@Preview
@Composable
private fun BottomNavPreview() {
    MyTasksTheme {
        var selectedIndex by remember{ mutableStateOf(0) }

        val homeTab = TabBarItem(
            index = 0,
            title = "Home",
            icon = Icons.Filled.Home,
            navigate = {}
        )

        val profileTab = TabBarItem(
            index = 1,
            title = "Profile",
            icon = Icons.Filled.Person,
            navigate = {}
        )

        TabBarView(
            selectedIndex = selectedIndex,
            selectIndex = {selectedIndex = it},
            fabItemNavigate = {},
            homeTab = homeTab,
            profileTab = profileTab
        )
    }
}