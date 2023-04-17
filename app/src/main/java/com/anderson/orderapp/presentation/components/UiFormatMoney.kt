package com.anderson.orderapp.presentation.components

import androidx.compose.runtime.Composable
import java.text.NumberFormat

@Composable
fun Double.asMoney(): String {
   return this.toMoney()
}

fun Double.toMoney(): String {
    return NumberFormat.getCurrencyInstance().format(this)
}
