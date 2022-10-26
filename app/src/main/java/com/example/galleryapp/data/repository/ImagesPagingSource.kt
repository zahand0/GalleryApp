package com.example.galleryapp.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import javax.inject.Inject

class ImagesPagingSource @Inject constructor(
    private val localStorage: LocalStorage
) : PagingSource<Int, ImageData>() {
    override fun getRefreshKey(state: PagingState<Int, ImageData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
        val position = params.key ?: -1
        return try {
            val images: MutableList<ImageData> = mutableListOf()
            localStorage.getAllImagesData(
                imagesOffset =  position,
                imagesLimit = params.loadSize
            ).collect { imageDataList ->
                images.addAll(imageDataList)
            }
            LoadResult.Page(
                data = images,
                prevKey = if (position == -1) null else position,
                nextKey = if (images.isEmpty()) null else position + params.loadSize
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}