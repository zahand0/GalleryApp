package com.example.galleryapp.domain.use_cases.get_image

import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow

class GetImageDataByNameUseCase(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(imageName: String): Flow<ImageData?> {
        return localStorage.getImageDataByName(imageName = imageName)
    }

}