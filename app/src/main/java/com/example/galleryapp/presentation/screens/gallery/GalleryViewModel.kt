package com.example.galleryapp.presentation.screens.gallery

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val useCases: UseCases
) : ViewModel() {

    private val _imagesData = mutableStateOf<List<ImageData>>(listOf())
    val imagesData: State<List<ImageData>> = _imagesData

    fun loadImagesData() {
        viewModelScope.launch(Dispatchers.IO) {
            _imagesData.value = useCases.getAllImagesDataUseCase()
        }
        Log.d("GalleryViewModel", "loadImagesData:")
    }

}