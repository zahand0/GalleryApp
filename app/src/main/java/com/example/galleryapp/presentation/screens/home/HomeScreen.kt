package com.example.galleryapp.presentation.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.galleryapp.R
import com.example.galleryapp.navigation.Screen
import com.example.galleryapp.ui.theme.bigFontSize
import com.example.galleryapp.ui.theme.buttonDefaultElevation
import com.example.galleryapp.ui.theme.buttonPressedElevation

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val imageData = homeViewModel.imageData.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        imageData.value?.let { image ->
            AsyncImage(
                model = image.imageUri,
                contentDescription = null,
                modifier = modifier
                    .fillMaxHeight(0.8f)
            )
        }
        GalleryButton(
            onClick = {
                navController.navigate(Screen.Gallery.route)
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
            fontSize = bigFontSize
        )
    }


}