package com.anderson.orderapp.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NavigationViewModel constructor() : ViewModel() {

    private val _navigation = MutableStateFlow<NavigationState>(NavigationState.EmptyNavigationState)
    val navigation = _navigation.asSharedFlow()

    fun push(route: NavigationScreen, args: List<Any>? = null) {
        viewModelScope.launch {
            _navigation.emit(NavigationState.PushNavigationState(extractRoute(route, args)))
        }
    }

    private fun extractRoute(route: NavigationScreen, args: List<Any>? = null) : String{
        return "${route}${args?.let {  "/" + it.joinToString("/") {item-> "$item" }} ?: ""}"
    }

    fun pop() {
        viewModelScope.launch {
            _navigation.emit(NavigationState.PopNavigationState)
        }
    }
}