package com.example.galleryapp.presentation.screens.home

import android.Manifest
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.use_cases.UseCases
import com.example.galleryapp.util.Constants.DETAILS_ARGUMENT_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _imageData: MutableStateFlow<ImageData?> = MutableStateFlow(null)
    val imageData: StateFlow<ImageData?> = _imageData

    private val _permissionsList = mutableListOf(
        Manifest.permission.READ_EXTERNAL_STORAGE
    )
    val permissionsList: List<String> = _permissionsList

    init {
        if (Build.VERSION.SDK_INT >= 29) {
            _permissionsList.add(Manifest.permission.ACCESS_MEDIA_LOCATION)
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val imageId = savedStateHandle.get<String>(DETAILS_ARGUMENT_KEY)
            if (!imageId.isNullOrEmpty()) {
                try {
                    useCases.getImageDataByIdUseCase(imageId = imageId).collectLatest {
                        _imageData.value = it
                    }
                } catch (e: Exception) {
                    Log.d("HomeViewModel", "Unable to load using id image: $e")
                    try {
                        _imageData.value = ImageData(
                            id = "",
                            imageUri = Uri.parse(
                                withContext(Dispatchers.IO) {
                                    URLDecoder.decode(
                                        imageId,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                }
                            ),
                            imageName = ""
                        )
                    } catch (e: Exception) {
                        Log.d("HomeViewModel", "Unable to decode image uri: $e")
                    }
                }
            }
        }
    }
}