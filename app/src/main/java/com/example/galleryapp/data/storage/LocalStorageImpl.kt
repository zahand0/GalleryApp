package com.example.galleryapp.data.storage

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
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
        selection: String?,
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

            val query = context.contentResolver.query(
                collection,
                projection,
                selection,
                null,
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
    }

    override suspend fun getImageDataById(imageId: String): Flow<ImageData?> {
        val selection = "${MediaStore.Images.Media._ID} = $imageId"
        return getAllImagesData(
            selection = selection,
            imagesOffset = -1,
            imagesLimit = 1
        ).map {
            it.getOrNull(0)
        }
    }

    override suspend fun getImageDataByName(imageName: String): Flow<ImageData?> {
        Log.d("LocalStorageImpl", "getImageData: imageName: $imageName")
        val selection = "${MediaStore.Images.Media.DISPLAY_NAME} = $imageName"
        return getAllImagesData(
            selection = selection,
            imagesOffset = -1,
            imagesLimit = 1
        ).map {
            it.getOrNull(0)
        }
    }

}