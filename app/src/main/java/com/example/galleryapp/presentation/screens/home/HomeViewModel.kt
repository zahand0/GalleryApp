package com.example.galleryapp.presentation.screens.home

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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    useCases: UseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _imageData: MutableStateFlow<ImageData?> = MutableStateFlow(null)
    val imageData: StateFlow<ImageData?> = _imageData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val imageId = savedStateHandle.get<String>(DETAILS_ARGUMENT_KEY)
            imageId?.let {
                useCases.getImageDataUseCase(imageId = imageId).collectLatest {
                    _imageData.value = it
                }
            }
        }
    }
}