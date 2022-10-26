package com.example.galleryapp

import android.Manifest.permission.ACCESS_MEDIA_LOCATION
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.galleryapp.navigation.SetupNavGraph
import com.example.galleryapp.ui.theme.GalleryAppTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint

@ExperimentalAnimationApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {}

        requestPermissions()

        setContent {
            GalleryAppTheme {
                navController = rememberAnimatedNavController()
                SetupNavGraph(navController = navController)
            }
        }
    }

    private fun checkPermissions(): Map<String, Boolean> {
        val permissions = mutableMapOf<String, Boolean>()
        permissions[READ_EXTERNAL_STORAGE] = ContextCompat.checkSelfPermission(
            this.applicationContext,
            READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
        if (Build.VERSION.SDK_INT >= 29) {
            permissions[ACCESS_MEDIA_LOCATION] = ContextCompat.checkSelfPermission(
                this.applicationContext,
                ACCESS_MEDIA_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }
        return permissions
    }

    private fun requestPermissions() {
        val requiredPermissions = checkPermissions().filter { (_: String, isGranted: Boolean) ->
            !isGranted
        }.keys.toTypedArray()
        if (requiredPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                requiredPermissions,
                101
            )
        }
    }

}
