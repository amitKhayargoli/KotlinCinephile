package com.example.cinephile.ui.activity.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

class ImageUtils(
    private val activityResultLauncher: ActivityResultLauncher<Intent>,
    private val permissionLauncher: ActivityResultLauncher<String>,
    private val callback: (Uri?) -> Unit
) {
    fun launchGallery(context: Context) {
        val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            android.Manifest.permission.READ_MEDIA_IMAGES
        } else {
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            Log.d("ImageUtils", "Requesting permission: $permission")
            permissionLauncher.launch(permission)
        } else {
            Log.d("ImageUtils", "Permission granted, opening gallery")
            openGallery()
        }
    }


    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        Log.d("ImageUtils", "Launching gallery intent")
        activityResultLauncher.launch(intent)
    }

}
