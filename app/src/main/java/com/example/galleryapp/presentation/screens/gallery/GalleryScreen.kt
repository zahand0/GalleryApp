package com.example.galleryapp.presentation.screens.gallery

import android.content.Context
import android.util.Log
import androidx.camera.view.PreviewView
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.galleryapp.presentation.widgets.CameraView
import com.example.galleryapp.presentation.widgets.DialogBox
import com.example.galleryapp.presentation.widgets.RequestPermissions
import com.example.galleryapp.presentation.widgets.rememberRequestPermissionsState
import com.example.galleryapp.ui.theme.errorColor
import com.example.galleryapp.ui.theme.mediumSpacing
import com.example.galleryapp.ui.theme.smallPadding
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_HEIGHT
import com.example.galleryapp.util.Constants.DEFAULT_THUMBNAIL_WIDTH
import com.example.galleryapp.util.Constants.GALLERY_IMAGES_PER_ROW
import com.example.galleryapp.util.navigateToAppSettings

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
                context = LocalContext.current,
                modifier = modifier
                    .padding(padding)
                    .background(MaterialTheme.colors.background)
            )
        }
    }
}

@Composable
fun ImageGrid(
    galleryViewModel: GalleryViewModel,
    navController: NavHostController,
    context: Context,
    modifier: Modifier = Modifier
) {

    val imgDataItems = galleryViewModel.imageDataFlow.collectAsLazyPagingItems()

    val multiplePermissionsState = rememberRequestPermissionsState(
        initRequest = true,
        permissions = galleryViewModel.permissionsList
    )
    var callRequestPermission by remember {
        mutableStateOf(true)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var cameraAvailable by remember {
        galleryViewModel.cameraAvailable
    }

    LaunchedEffect(key1 = callRequestPermission) {
        Log.d("GalleryScreen", "callRequestPermission: $callRequestPermission")
    }

    if (callRequestPermission) {
        RequestPermissions(
            context = context,
            requestState = multiplePermissionsState,
            permissionsGrantedCase = {
                if (cameraAvailable) {
                    navController.navigate(Screen.Photo.route)
                }
                cameraAvailable = true
            },
            permissionsDeniedCase = {
                callRequestPermission = false
                showDialog = true
            }
        )
    }

    if (showDialog) {
        DialogBox(
            title = stringResource(R.string.camera_permission),
            description = stringResource(R.string.camera_access_prompt),
            acceptString = stringResource(R.string.settings),
            deniedString = stringResource(R.string.later),
            onDismiss = {
                showDialog = false
            },
            onSettingsClick = {
                context.navigateToAppSettings()
            }
        )
    }

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
            CameraPreview(
                cameraAvailable = cameraAvailable
            ) {
                multiplePermissionsState.request = true
                callRequestPermission = true
            }
        }

        items(imgDataItems.itemCount) { index ->
            imgDataItems[index]?.let { imageData ->
                ImageThumbnail(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface),
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
    cameraAvailable: Boolean,
    onClick: () -> Unit
) {
    Box(modifier = modifier
        .size(120.dp)
        .clickable { onClick() }
    ) {
        if (cameraAvailable) {
            CameraView(
                scaleType = PreviewView.ScaleType.FILL_CENTER
            )
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
                        .blur(
                            radius = 6.dp
                        )
                        .padding(18.dp)
                )
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera),
                    contentDescription = stringResource(R.string.camera),
                    tint = Color.White,
                    modifier = Modifier
                        .fillMaxSize(0.7f)
                )
            }
        } else {
            WarningCameraPreview()
        }
    }
}

@Preview
@Composable
fun WarningCameraPreview() {
    Column(
        modifier = Modifier
            .background(Color.Black.copy(alpha = 0.4f))
            .size(120.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_camera),
                contentDescription = stringResource(R.string.warning),
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_warning),
                contentDescription = stringResource(R.string.warning),
                modifier = Modifier
                    .size(30.dp)
                    .offset(
                        28.dp,
                        (-10).dp
                    ),
                tint = MaterialTheme.colors.errorColor
            )
        }
        Text(
            text = stringResource(R.string.camera_warning), color = Color.White,
            modifier = Modifier.padding(horizontal = 6.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}
