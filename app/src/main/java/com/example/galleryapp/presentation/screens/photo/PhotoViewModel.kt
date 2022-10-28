package com.example.galleryapp.presentation.screens.photo

import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor() : ViewModel() {

    private val _imageUri: MutableStateFlow<String?> = MutableStateFlow(null)
    val imageUri: StateFlow<String?> = _imageUri

    private val _imageCreated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val imageCreated: StateFlow<Boolean> = _imageCreated

    fun takePhoto(
        imageCapture: ImageCapture,
        outputOptions: OutputFileOptions,
        executor: Executor
    ) {
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exception: ImageCaptureException) {
                    Log.e("PhotoViewModel", "Can't save image: $exception")
                }

                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri?.toString() ?: ""
                    _imageUri.value = URLEncoder.encode(uri, StandardCharsets.UTF_8.toString())
                    _imageCreated.value = true
                }
            })

    }

}