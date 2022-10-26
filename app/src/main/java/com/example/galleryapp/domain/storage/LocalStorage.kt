package com.example.galleryapp.domain.storage

import android.graphics.Bitmap
import android.net.Uri
import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow

interface LocalStorage {
    suspend fun getAllImagesData(
        imagesOffset: Int,
        imagesLimit: Int,
        imageId: String? = null
    ): Flow<List<ImageData>>
    suspend fun getImageData(imageId: String): Flow<ImageData?>
//    suspend fun load(params: PagingSource.LoadParams<Int>): PagingSource.LoadResult<Int, ImageData>
}