package com.reringuy.support.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationMap(val route: String) {
    @Serializable
    object Home : NavigationMap("Home")
    @Serializable
    object TaskList : NavigationMap("Tasks List")
    @Serializable
    object NewTask : NavigationMap("New Task")
    @Serializable
    data class TaskDetails(val taskId: Long) : NavigationMap("Task N°$taskId Detail")
}

enum class Destination(
    val icon: ImageVector,
    val contentDescription: String,
    val route: NavigationMap
) {
    HOME(icon = Icons.Filled.Home, contentDescription = "Pagina inicial", route = NavigationMap.Home),
    ATIVIDADES(icon = Icons.AutoMirrored.Filled.FormatListBulleted, contentDescription = "Minhas atividades", route = NavigationMap.TaskList)
}