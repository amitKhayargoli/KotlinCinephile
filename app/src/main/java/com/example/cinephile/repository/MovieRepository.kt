package com.example.cinephile.repository

import android.content.Context
import android.net.Uri
import com.example.cinephile.model.MovieModel

interface MovieRepository {

    fun addMovies(movieModel: MovieModel,
    callback:(Boolean,String)->Unit)

    fun updateMovie(movieId:String,
                    data:MutableMap<String,Any>,
                    callback: (Boolean, String) -> Unit)

    fun deleteMovie(movieId:String,
                    callback: (Boolean, String) -> Unit)

    fun getMovieById(movieId: String,
                     callback: (MovieModel?,Boolean, String) -> Unit)

    fun getAllMovies(callback: (List<MovieModel>?,Boolean, String) -> Unit)


//    fun getMovieByid(callback: (List<MovieModel>?, Boolean, String) -> Unit)


}