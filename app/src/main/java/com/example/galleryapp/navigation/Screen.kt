package com.example.galleryapp.navigation

import com.example.galleryapp.util.Constants.DETAILS_ARGUMENT_KEY

sealed class Screen(val route: String) {
    object Gallery: Screen("gallery_screen")
    object Home: Screen("home_screen/{$DETAILS_ARGUMENT_KEY}") {
        fun passImageId(imageId: String): String {
            return "home_screen/$imageId"
        }
    }
}
