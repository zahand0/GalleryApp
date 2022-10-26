package com.example.galleryapp.presentation.screens.gallery

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.ui.theme.mediumSpacing
import com.example.galleryapp.ui.theme.smallPadding
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_HEIGHT
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_WIDTH
import com.example.galleryapp.util.Constants.GALLERY_IMAGES_PER_ROW

@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    galleryViewModel: GalleryViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        galleryViewModel.loadImagesData()
    }
    Scaffold(
        topBar = {
            GalleryTopBar(onBackClicked = {
                navController.popBackStack()
            })
        }
    ) { padding ->
        ImageGrid(
            galleryViewModel = galleryViewModel,
            navController = navController,
            modifier = modifier
                .padding(padding)
        )
    }
}

@Composable
fun ImageGrid(
    galleryViewModel: GalleryViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val imgDataList = galleryViewModel.imagesData

    LazyVerticalGrid(
        columns = GridCells.Fixed(count = GALLERY_IMAGES_PER_ROW),
        contentPadding = PaddingValues(
            horizontal = smallPadding,
            vertical = smallPadding
        ),
        verticalArrangement = Arrangement.spacedBy(mediumSpacing),
        horizontalArrangement = Arrangement.spacedBy(mediumSpacing),
        modifier = modifier
    ) {
        items(imgDataList.value) { imageData ->
            ImageEntry(
                imageData = imageData,
                onClicked = {
                    navController.navigate(Screen.Home.passImageId(imageData.id)){
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun ImageEntry(
    imageData: ImageData,
    onClicked: () -> Unit
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageData.imageUri)
            .size(
                size = Size(
                    width = DEFAULT_THUMBNAIL_WIDTH,
                    height = DEFAULT_THUMBNAIL_HEIGHT
                )
            )
            .scale(Scale.FILL)
            .crossfade(true)
            .build(),
        contentDescription = imageData.imageName,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(120.dp)
            .clickable {
                onClicked()
            }
    )
}