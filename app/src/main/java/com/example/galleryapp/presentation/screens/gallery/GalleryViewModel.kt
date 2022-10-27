package com.example.galleryapp.presentation.screens.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.galleryapp.data.repository.ImagesPagingSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val imagesPagingSource: ImagesPagingSource
) : ViewModel() {

    val imageDataFlow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 100)
    ) {
        imagesPagingSource
    }.flow
        .cachedIn(viewModelScope)

}