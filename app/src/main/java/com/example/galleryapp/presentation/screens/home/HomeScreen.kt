package com.example.galleryapp.presentation.screens.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.galleryapp.R
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.presentation.widgets.DialogBox
import com.example.galleryapp.presentation.widgets.RequestPermissions
import com.example.galleryapp.presentation.widgets.rememberRequestPermissionsState
import com.example.galleryapp.ui.theme.*
import com.example.galleryapp.util.navigateToAppSettings

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val multiplePermissionsState = rememberRequestPermissionsState(
        initRequest = true,
        permissions = homeViewModel.permissionsList
    )
    var callRequestPermission by remember {
        mutableStateOf(false)
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    if (callRequestPermission) {
        RequestPermissions(
            context = LocalContext.current,
            requestState = multiplePermissionsState,
            permissionsGrantedCase = {
                navController.navigate(Screen.Gallery.route)
            },
            permissionsDeniedCase = {
                callRequestPermission = false
                showDialog = true
            }
        )
    }
    if (showDialog) {
        DialogBox(
            title = stringResource(R.string.storage_permission),
            description = stringResource(R.string.photos_access_prompt),
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
    val imageData = homeViewModel.imageData.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageData.value?.let { image ->
            SelectedImagePreview(
                imageUri = image.imageUri
            )
        }
        GalleryButton(
            onClick = {
                multiplePermissionsState.request = true
                callRequestPermission = true
            },
            modifier = Modifier
        )
    }
}

@Composable
fun GalleryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        modifier = modifier
            .fillMaxWidth(0.4f),
        elevation = ButtonDefaults.elevation(
            defaultElevation = buttonDefaultElevation,
            pressedElevation = buttonPressedElevation
        )
    ) {
        Text(
            text = stringResource(R.string.gallery),
            fontSize = bigFontSize,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun SelectedImagePreview(
    modifier: Modifier = Modifier,
    imageUri: Uri
) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        scale *= zoomChange
        offset += offsetChange
    }
    Box(
        modifier = modifier
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationX = offset.x,
                translationY = offset.y
            )
            .transformable(state)
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight(0.8f)

        )
    }
}