package com.example.galleryapp.domain.storage

import android.net.Uri

data class ImageData(
    val id: String,
    val imageUri: Uri,
    val imageName: String
)
