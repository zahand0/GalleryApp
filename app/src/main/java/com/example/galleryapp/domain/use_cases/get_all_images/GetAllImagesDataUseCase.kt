package com.example.galleryapp.domain.use_cases.get_all_images

import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage

class GetAllImagesDataUseCase(
    private val localStorage: LocalStorage
) {
    suspend operator fun invoke(): List<ImageData> {
        return localStorage.getAllImagesData()
    }
}