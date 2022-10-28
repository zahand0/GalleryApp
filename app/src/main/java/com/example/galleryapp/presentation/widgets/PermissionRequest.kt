package com.example.galleryapp.presentation.widgets

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.*
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

class RequestPermissionState(initRequest: Boolean, val permissions: List<String>) {
    var request by mutableStateOf(initRequest)
}

@Composable
fun rememberRequestPermissionsState(
    initRequest: Boolean = false,
    permissions: List<String>
): RequestPermissionState {
    return remember {
        RequestPermissionState(initRequest, permissions)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    context: Context,
    requestState: RequestPermissionState,
    permissionsGrantedCase: () -> Unit,
    permissionsDeniedCase: () -> Unit
) {
    val state = rememberMultiplePermissionsState(permissions = requestState.permissions) {
        val permissionsPermanentlyDenied = try {
            val activity = context.findActivity()
            it.filter { (permission: String, isGranted: Boolean) ->
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    permission
                ) || !isGranted
            }.isNotEmpty()
        } catch (e: Exception) {
            true
        }
        if (permissionsPermanentlyDenied) {
            permissionsDeniedCase()
        } else {
            permissionsGrantedCase()
        }
    }

    if (requestState.request) {
        requestState.request = false
        if (state.allPermissionsGranted) {
            permissionsGrantedCase()
        } else {
            LaunchedEffect(key1 = requestState.request) {
                state.launchMultiplePermissionRequest()
            }
        }
    }
}

internal tailrec fun Context.findActivity(): Activity =
    when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.findActivity()
        else -> throw IllegalArgumentException("Could not find activity!")
    }