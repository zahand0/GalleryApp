package com.example.galleryapp.presentation.screens.photo

import androidx.camera.core.ImageCapture
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.presentation.widgets.CameraView
import java.util.concurrent.Executor

@Composable
fun PhotoScreen(
    navController: NavHostController,
    outputOptions: ImageCapture.OutputFileOptions,
    executor: Executor,
    photoViewModel: PhotoViewModel = hiltViewModel()
) {
    val imageUri = photoViewModel.imageUri.collectAsState()
    val imageCreated = photoViewModel.imageCreated.collectAsState()

    CameraView(
        modifier = Modifier
            .fillMaxSize()
    ) { imageCapture ->
        photoViewModel.takePhoto(
            imageCapture = imageCapture,
            outputOptions = outputOptions,
            executor = executor
        )
    }
    if (imageCreated.value) {
        navController.navigate(
            Screen.Home.passImageUri(
                imageUri = imageUri.value ?: ""
            )
        ) {
            popUpTo(Screen.Home.route) {
                inclusive = true
            }
        }
    }
}