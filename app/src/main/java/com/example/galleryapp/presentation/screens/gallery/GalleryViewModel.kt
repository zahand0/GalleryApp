package com.example.galleryapp.presentation.screens.gallery

import android.Manifest
import android.os.Build
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

    private val _permissionsList = mutableListOf(
        Manifest.permission.CAMERA
    )
    val permissionsList: List<String> = _permissionsList

    init {
        if (Build.VERSION.SDK_INT <= 28) {
            _permissionsList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    val imageDataFlow = Pager(
        // Configure how data is loaded by passing additional properties to
        // PagingConfig, such as prefetchDistance.
        PagingConfig(pageSize = 60)
    ) {
        imagesPagingSource
    }.flow
        .cachedIn(viewModelScope)

}