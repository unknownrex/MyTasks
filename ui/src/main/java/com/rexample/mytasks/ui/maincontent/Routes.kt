package com.rexample.mytasks.ui.maincontent

import kotlinx.serialization.Serializable

@Serializable
object HomeViewRoute

@Serializable
object AddTaskRoute

@Serializable
data class EditTaskRoute(val taskId: Int)

@Serializable
object ProfileRoute

@Serializable
object ManageCategoryRoute
