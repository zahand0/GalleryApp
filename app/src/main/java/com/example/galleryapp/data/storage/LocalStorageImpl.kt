package com.example.galleryapp.data.storage

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Size
import androidx.annotation.RequiresApi
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.domain.storage.LocalStorage
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_HEIGHT
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_WIDTH
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalStorageImpl(
    private val context: Context
) : LocalStorage {

    override suspend fun getAllImagesData(
        imageId: String?
    ): List<ImageData> {
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

            val selection = imageId?.let { "${MediaStore.Images.Media._ID} = ?"}
            val selectionArgs = imageId?.let { arrayOf(imageId)}

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

                while (cursor.moveToNext()) {
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
        return imgList
    }

    override suspend fun getImageData(imageId: String): ImageData? {
        return getAllImagesData(imageId = imageId).getOrNull(0)
    }


}