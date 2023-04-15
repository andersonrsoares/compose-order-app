package com.anderson.orderapp.presentation

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

sealed class UiText {
    class DynamicString(val text: String?): UiText()
    class ResourceString(@StringRes val resId: Int) : UiText()
}

internal fun UiText.getString(context: Context): String {
    return when (this) {
        is UiText.DynamicString -> this.text.orEmpty()
        is UiText.ResourceString -> context.getString(this.resId)
    }
}

@Composable
fun UiText.asString(): String {
    return this.getString(LocalContext.current)
}


