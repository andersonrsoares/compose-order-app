package com.anderson.orderapp.pizza_menu

import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import com.anderson.orderapp.domain.DataState
import com.anderson.orderapp.domain.model.Pizza
import com.anderson.orderapp.domain.repository.OrderRepository
import com.anderson.orderapp.domain.repository.PizzasRepository
import com.anderson.orderapp.presentation.navigation.NavigationViewModel
import com.anderson.orderapp.presentation.pizza_menu.OrderPizzaScreen
import com.anderson.orderapp.presentation.pizza_menu.PizzaMenuViewModel
import com.anderson.orderapp.ui.theme.OrderappTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.dsl.module

class PizzaMenuScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()
    // use createAndroidComposeRule<YourActivity>() if you need access to
    // an activity
    private val pizzasRepository =  mockk<PizzasRepository>()
    private val orderRepository =  mockk<OrderRepository>()
    private val viewmodelModule = module {
        viewModel {
            PizzaMenuViewModel(pizzasRepository, orderRepository)
            NavigationViewModel()
        }
    }

    @Before
    fun setUp() {
       loadKoinModules(listOf((viewmodelModule)))
    }

    @After
    fun shutdown() {
        unloadKoinModules(listOf((viewmodelModule)))
    }

    @Test
    fun loadScreen() {
        every { pizzasRepository.fetchPizzas() } returns flow {
            emit(DataState.Success(arrayListOf(
                Pizza("flavor 1", 10.0),
                Pizza("flavor 2", 20.0)
            )))
        }
        // Start the app
        composeTestRule.setContent {
            OrderappTheme {
                OrderPizzaScreen()
            }
        }

        composeTestRule
            .onNode(hasText("flavor 1"))
    }
}