package com.example.galleryapp.domain.use_cases.get_image

import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow

class GetImageDataUseCase(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(imageId: String): Flow<ImageData?> {
        return localStorage.getImageData(imageId = imageId)
    }
}