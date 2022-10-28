package com.example.galleryapp.navigation

import androidx.camera.core.ImageCapture
import androidx.compose.animation.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.galleryapp.presentation.screens.gallery.GalleryScreen
import com.example.galleryapp.presentation.screens.home.HomeScreen
import com.example.galleryapp.presentation.screens.photo.PhotoScreen
import com.example.galleryapp.util.Constants.DETAILS_ARGUMENT_KEY
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import java.util.concurrent.Executor

@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    outputOptions: ImageCapture.OutputFileOptions,
    executor: Executor
) {
    val systemUiController: SystemUiController = rememberSystemUiController()
    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Gallery.route,
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it })
            },
            exitTransition = {
                slideOutHorizontally { it }
            }
        ) {
            GalleryScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
            exitTransition = null,
            arguments = listOf(navArgument(DETAILS_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            route = Screen.Photo.route,
            enterTransition = {
                systemUiController.isStatusBarVisible = false
                fadeIn()
            },
            exitTransition = {
                systemUiController.isStatusBarVisible = true
                fadeOut()
            }
        ) {
            PhotoScreen(
                navController = navController,
                outputOptions = outputOptions,
                executor = executor
            )
        }
    }
}