package com.example.galleryapp.domain.use_cases.get_image

import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage

class GetImageDataUseCase(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(imageId: String): ImageData? {
        return localStorage.getImageData(imageId = imageId)
    }
}