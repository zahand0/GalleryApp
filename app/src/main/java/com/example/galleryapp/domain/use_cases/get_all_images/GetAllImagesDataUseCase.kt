package com.example.galleryapp.domain.use_cases.get_all_images

import androidx.paging.PagingSource.LoadResult
import androidx.paging.PagingSource.LoadParams
import com.example.galleryapp.data.repository.ImagesPagingSource
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import kotlinx.coroutines.flow.Flow

class GetAllImagesDataUseCase(
//    private val localStorage: LocalStorage
    private val imagesPagingSource: ImagesPagingSource
) {
    suspend operator fun invoke(
        params: LoadParams<Int>
    ): LoadResult<Int, ImageData> {
//        return localStorage.getAllImagesData(
//            imagesOffset = imageOffset,
//            imagesLimit = imageLimit
//        )
        return imagesPagingSource.load(params = params)
    }
}