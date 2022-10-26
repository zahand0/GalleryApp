package com.example.galleryapp.data.storage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalStorageImpl(
    private val context: Context
) : LocalStorage {

    override suspend fun getAllImagesData(
        imagesOffset: Int,
        imagesLimit: Int,
        imageId: String?,
    ): Flow<List<ImageData>> = flow {
        val imgList: MutableList<ImageData> = mutableListOf()

        withContext(Dispatchers.IO) {

            val collection =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Images.Media.getContentUri(
                        MediaStore.VOLUME_EXTERNAL
                    )
                } else {
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }

            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

            val selection = imageId?.let { "${MediaStore.Images.Media._ID} = ?" }
            val selectionArgs = imageId?.let { arrayOf(imageId) }

            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )
            query?.use { cursor ->
                // Cache column indices.
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

                cursor.moveToPosition(imagesOffset)

                while (cursor.moveToNext() && cursor.position <= imagesOffset + imagesLimit) {
                    // Get values of columns for a given image.
                    val id = cursor.getLong(idColumn)
                    val name = cursor.getString(nameColumn)

                    val contentUri: Uri = ContentUris.withAppendedId(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        id
                    )

                    // Stores column values and the contentUri in a local object
                    // that represents the media file.
                    imgList.add(
                        ImageData(
                            id = id.toString(),
                            imageUri = contentUri,
                            imageName = name
                        )
                    )
                }
            }
        }
        emit(imgList)
//        return imgList
    }

    override suspend fun getImageData(imageId: String): Flow<ImageData?> {
        return getAllImagesData(
            imageId = imageId,
            imagesOffset = -1,
            imagesLimit = 1
        ).map {
            it.getOrNull(0)
        }
    }

//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageData> {
//        val position = params.key ?: 0
//        return try {
//            val images: MutableList<ImageData> = mutableListOf()
//            getAllImagesData(
//                imagesOffset =  position,
//                imagesLimit = params.loadSize
//            ).collect { imageDataList ->
//                images.addAll(imageDataList)
//            }
//            LoadResult.Page(
//                data = images,
//                prevKey = if (position == 0) null else position,
//                nextKey = if (images.isEmpty()) null else position + params.loadSize
//            )
//        } catch (exception: Exception) {
//            LoadResult.Error(exception)
//        }
//    }


}