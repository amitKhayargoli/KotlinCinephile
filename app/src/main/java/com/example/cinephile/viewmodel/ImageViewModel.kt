package com.example.cinephile.viewmodel

import android.content.Context
import android.net.Uri
import com.example.cinephile.repository.ImageRepository

class ImageViewModel(val repository: ImageRepository){

    fun uploadImage(context: Context, imageUri: Uri, callback: (String?) -> Unit){
        repository.uploadImage(context, imageUri, callback)
    }

}