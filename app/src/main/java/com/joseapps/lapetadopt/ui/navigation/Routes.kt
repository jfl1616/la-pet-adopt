package com.joseapps.lapetadopt.ui.navigation

sealed class Routes(val route: String) {
    data object Browse : Routes("browse")
    data object Favorites : Routes("favorites")
    data object Detail : Routes("detail/{petId}") {
        fun createRoute(petId: Long) = "detail/$petId"
    }
}
