package com.anderson.orderapp.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
internal fun ShowMessage(message:String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(color = Color.Black,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = message)
    }
}

@Composable
internal fun ShowErrorMessage(error:String) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(color = Color.Black,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = 30.sp,
            text = error)
    }
}