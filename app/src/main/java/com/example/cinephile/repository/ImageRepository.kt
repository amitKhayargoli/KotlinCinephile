package com.example.cinephile.repository

import android.content.Context
import android.net.Uri

interface ImageRepository {
    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit)

    fun getFileNameFromUri(context: Context, uri: Uri): String?
}