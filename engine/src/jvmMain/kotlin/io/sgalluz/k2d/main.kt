package io.sgalluz.k2d

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "K2D",
    ) {
        App()
    }
}