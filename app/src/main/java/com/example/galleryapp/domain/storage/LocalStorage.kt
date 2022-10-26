package com.example.galleryapp.domain.storage

import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    suspend fun getAllImagesData(
        imagesOffset: Int,
        imagesLimit: Int,
        imageId: String? = null
    ): Flow<List<ImageData>>

    suspend fun getImageData(imageId: String): Flow<ImageData?>
}