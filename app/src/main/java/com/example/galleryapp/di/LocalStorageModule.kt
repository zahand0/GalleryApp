package com.example.galleryapp.di

import android.content.Context
import com.example.galleryapp.data.repository.ImagesPagingSource
import com.example.galleryapp.data.storage.LocalStorageImpl
import com.example.galleryapp.domain.storage.LocalStorage
import com.example.galleryapp.domain.use_cases.UseCases
import com.example.galleryapp.domain.use_cases.get_all_images.GetAllImagesDataUseCase
import com.example.galleryapp.domain.use_cases.get_image.GetImageDataByIdUseCase
import com.example.galleryapp.domain.use_cases.get_image.GetImageDataByNameUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalStorageModule {

    @Provides
    @Singleton
    fun provideLocalStorage(@ApplicationContext context: Context): LocalStorage {
        return LocalStorageImpl(context)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        localStorage: LocalStorage,
        imagesPagingSource: ImagesPagingSource
    ): UseCases {
        return UseCases(
            getAllImagesDataUseCase = GetAllImagesDataUseCase(
                imagesPagingSource = imagesPagingSource
            ),
            getImageDataByIdUseCase = GetImageDataByIdUseCase(localStorage = localStorage),
            getImageDataByNameUseCase = GetImageDataByNameUseCase(localStorage = localStorage),
        )
    }
}