package com.example.galleryapp.domain.storage

import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    suspend fun getAllImagesData(
        imagesOffset: Int,
        imagesLimit: Int,
        selection: String? = null
    ): Flow<List<ImageData>>

    suspend fun getImageDataById(imageId: String): Flow<ImageData?>
    suspend fun getImageDataByName(imageName: String): Flow<ImageData?>
}