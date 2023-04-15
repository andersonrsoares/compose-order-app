package com.anderson.orderapp.presentation.navigation

sealed class NavigationState {
    object EmptyNavigationState: NavigationState()
    object PopNavigationState: NavigationState()
    data class PushNavigationState(val destination: String): NavigationState()
}