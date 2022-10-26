package com.example.galleryapp.domain.use_cases.get_all_images

import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import com.example.galleryapp.data.repository.ImagesPagingSource
import com.example.galleryapp.domain.storage.ImageData

class GetAllImagesDataUseCase(
    private val imagesPagingSource: ImagesPagingSource
) {
    suspend operator fun invoke(
        params: LoadParams<Int>
    ): LoadResult<Int, ImageData> {
        return imagesPagingSource.load(params = params)
    }
}