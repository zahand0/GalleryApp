package com.example.galleryapp.presentation.screens.home

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.galleryapp.R
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.presentation.widgets.DialogBox
import com.example.galleryapp.presentation.widgets.RequestPermissions
import com.example.galleryapp.presentation.widgets.rememberRequestPermissionsState
import com.example.galleryapp.ui.theme.bigFontSize
import com.example.galleryapp.ui.theme.buttonDefaultElevation
import com.example.galleryapp.ui.theme.buttonPressedElevation
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
        if (imageData.value != null) {
            imageData.value?.let { image ->
                SelectedImagePreview(
                    imageUri = image.imageUri
                )
            }
        } else {
            DefaultImage()
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
    Box(
        modifier = modifier
    ) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxHeight(0.8f)

        )
    }
}

@Composable
fun DefaultImage() {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_moon),
        contentDescription = stringResource(R.string.home_image),
        tint = MaterialTheme.colors.primaryVariant,
        modifier = Modifier
            .fillMaxHeight(0.8f)
            .size(250.dp)
    )
}