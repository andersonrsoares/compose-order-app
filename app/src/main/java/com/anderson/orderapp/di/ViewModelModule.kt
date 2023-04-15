package com.anderson.orderapp.di

import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import com.anderson.orderapp.presentation.order_pizza.OrderPizzaViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel


val ViewModelModule = module {
    viewModel { NavigationViewModel() }
    viewModel { OrderPizzaViewModel(get()) }
}
