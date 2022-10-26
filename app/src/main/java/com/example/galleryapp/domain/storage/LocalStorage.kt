package com.example.galleryapp.domain.storage

import android.graphics.Bitmap
import android.net.Uri

interface LocalStorage {
    suspend fun getAllImagesData(imageId: String? = null): List<ImageData>
    suspend fun getImageData(imageId: String): ImageData?
}