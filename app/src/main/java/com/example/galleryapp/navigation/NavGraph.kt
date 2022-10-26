package com.example.galleryapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navArgument
import com.example.galleryapp.presentation.screens.gallery.GalleryScreen
import com.example.galleryapp.presentation.screens.home.HomeScreen
import com.example.galleryapp.util.Constants.DETAILS_ARGUMENT_KEY
import com.google.accompanist.navigation.animation.AnimatedNavHost

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Gallery.route
        ) {
            GalleryScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
            arguments = listOf(navArgument(DETAILS_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            HomeScreen(navController = navController)
        }
    }
}