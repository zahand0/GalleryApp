package com.example.galleryapp.presentation.widgets

import android.view.MotionEvent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Lens
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.galleryapp.util.getCameraProvider

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CameraView(
    modifier: Modifier = Modifier,
    scaleType: PreviewView.ScaleType = PreviewView.ScaleType.FILL_CENTER,
    lensFacing: Int = CameraSelector.LENS_FACING_BACK,
    onButtonClick: ((ImageCapture) -> Unit)? = null
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val preview = Preview.Builder().build()
    val previewView = remember {
        PreviewView(context).apply {
            this.scaleType = scaleType
        }
    }
    val imageCapture: ImageCapture = remember { ImageCapture.Builder().build() }
    val cameraSelector = CameraSelector.Builder()
        .requireLensFacing(lensFacing)
        .build()

    LaunchedEffect(lensFacing) {
        val cameraProvider = context.getCameraProvider()
        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            cameraSelector,
            preview,
            imageCapture
        )
        preview.setSurfaceProvider(previewView.surfaceProvider)
    }

    var shutterState by remember {
        mutableStateOf(false)
    }
    val shutterSize by animateDpAsState(
        targetValue = if (shutterState) 100.dp else 75.dp,
        animationSpec = tween(
            durationMillis = 400
        )
    )

    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(10f)
        ) {
            AndroidView(
                { previewView }, modifier = Modifier.fillMaxSize()

            )
        }

        onButtonClick?.let {

            Column(
                modifier = Modifier
                    .weight(2.0f)
            ) {
                IconButton(
                    modifier = Modifier
                        .pointerInteropFilter {
                            when (it.action) {
                                MotionEvent.ACTION_DOWN -> {
                                    shutterState = true
                                    onButtonClick(imageCapture)
                                }
                                MotionEvent.ACTION_UP -> {
                                    shutterState = false
                                }
                                MotionEvent.ACTION_CANCEL -> {
                                    shutterState = false
                                }
                            }
                            true
                        },
                    onClick = {},
                    content = {
                        Icon(
                            imageVector = Icons.Sharp.Lens,
                            contentDescription = "Take picture",
                            tint = Color.White,
                            modifier = Modifier
                                .size(shutterSize)
                                .border(3.dp, Color.White, CircleShape)
                                .padding(2.dp)
                        )
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
