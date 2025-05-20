package com.reringuy.support.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val icon: ImageVector,
    val contentDescription: String
) {
    HOME(icon = Icons.Filled.Home, contentDescription = "Pagina inicial"),
    Atividades(icon = Icons.Filled.Home, contentDescription = "Minhas atividades")
}