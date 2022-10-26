package com.example.galleryapp.presentation.screens.gallery

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.galleryapp.data.repository.ImagesPagingSource
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.use_cases.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
//    private val useCases: UseCases
private val imagesPagingSource: ImagesPagingSource
) : ViewModel() {

//    private val _imagesData = mutableStateOf<PagingData<ImageData>>(PagingData())
//    val imagesData: State<PagingData<ImageData>> = _imagesData

    val imageDataFlow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 100)
    ) {
        imagesPagingSource
    }.flow
        .cachedIn(viewModelScope)
//
//    fun loadImagesData() {
//        viewModelScope.launch(Dispatchers.IO) {
//            imagesFlow.collectLatest {
//                _imagesData.value = it
//            }
////            _imagesData.value = useCases.getAllImagesDataUseCase()
//        }
//        Log.d("GalleryViewModel", "loadImagesData:")
//    }

}