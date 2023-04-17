package com.anderson.orderapp.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.NumberFormat

@Composable
fun Double.asMoney(): String {
   return this.toMoney()
}

fun Double.toMoney(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}
