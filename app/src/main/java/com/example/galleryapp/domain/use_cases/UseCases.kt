package com.example.galleryapp.domain.use_cases

import com.example.galleryapp.domain.use_cases.get_all_images.GetAllImagesDataUseCase
import com.example.galleryapp.domain.use_cases.get_image.GetImageDataUseCase

data class UseCases(
    val getAllImagesDataUseCase: GetAllImagesDataUseCase,
    val getImageDataUseCase: GetImageDataUseCase
)
