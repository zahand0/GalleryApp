package com.example.galleryapp.domain.use_cases

import com.example.galleryapp.domain.use_cases.get_all_images.GetAllImagesDataUseCase
import com.example.galleryapp.domain.use_cases.get_image.GetImageDataByIdUseCase
import com.example.galleryapp.domain.use_cases.get_image.GetImageDataByNameUseCase

data class UseCases(
    val getAllImagesDataUseCase: GetAllImagesDataUseCase,
    val getImageDataByIdUseCase: GetImageDataByIdUseCase,
    val getImageDataByNameUseCase: GetImageDataByNameUseCase
)
