package com.example.galleryapp.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

val Red100 = Color(0xFFffbcaf)
val Red500 = Color(0xFFf44336)
val Red700 = Color(0xFFd32f2f)

val DarkGrey = Color(0xFF121212)

val DarkGreyLighter = Color(0xFF161616)

val LightCream100 = Color(0xfff9f9f7)
val LightCream200 = Color(0xfff4f3ef)
val LightCream300 = Color(0xffefeee8)
val LightCream400 = Color(0xffeae8e0)
val LightCream500 = Color(0xffe5e3d9)

val Colors.errorColor: Color
    @Composable
    get() = if (isLight) Color(0xFFE01030) else Color(0xFFB00020)