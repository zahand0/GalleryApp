package com.example.galleryapp.presentation.screens.gallery

import android.util.Log
import android.view.ViewGroup
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.example.galleryapp.R
import com.example.galleryapp.domain.storage.ImageData
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.ui.theme.mediumSpacing
import com.example.galleryapp.ui.theme.smallPadding
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_HEIGHT
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_WIDTH
import com.example.galleryapp.util.Constants.GALLERY_IMAGES_PER_ROW
import com.example.galleryapp.util.getCameraProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GalleryScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    galleryViewModel: GalleryViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            GalleryTopBar(onBackClick = {
                navController.popBackStack()
            })
        }
    ) { padding ->
        CompositionLocalProvider(
            LocalOverscrollConfiguration provides null
        ) {
            ImageGrid(
                galleryViewModel = galleryViewModel,
                navController = navController,
                modifier = modifier
                    .padding(padding)
            )
        }
    }
}

@Composable
fun ImageGrid(
    galleryViewModel: GalleryViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {

    val imgDataItems = galleryViewModel.imageDataFlow.collectAsLazyPagingItems()

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

        item {
            CameraMinimizePreview {

            }
        }

        items(imgDataItems.itemCount) { index ->
            imgDataItems[index]?.let { imageData ->
                ImageThumbnail(
                    imageData = imageData,
                    onClick = {
                        navController.navigate(Screen.Home.passImageId(imageData.id)) {
                            popUpTo(Screen.Home.route) {
                                inclusive = true
                            }
                        }
                    }
                )
            }
        }
    }

}

@Composable
fun ImageThumbnail(
    modifier: Modifier = Modifier,
    imageData: ImageData,
    onClick: () -> Unit
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
        modifier = modifier
            .size(120.dp)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
) {
    val coroutineScope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        modifier = modifier,
        factory = { context ->
            val previewView = PreviewView(context).apply {
                this.scaleType = scaleType
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // CameraX Preview UseCase
            val previewUseCase = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            coroutineScope.launch {
                val cameraProvider = context.getCameraProvider()
                try {
                    // Must unbind the use-cases before rebinding them.
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner, cameraSelector, previewUseCase
                    )
                } catch (ex: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", ex)
                }
            }

            previewView
        }
    )
}

@Composable
fun CameraMinimizePreview(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .size(120.dp)
        .clickable { onClick() }
    ) {
        CameraPreview()
        Box(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.4f))
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera),
                contentDescription = stringResource(R.string.camera),
                tint = Color.White,
                modifier = Modifier
                    .fillMaxSize(0.7f)
            )
        }
    }

}