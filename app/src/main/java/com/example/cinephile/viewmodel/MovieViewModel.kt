package com.example.cinephile.viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.cinephile.model.MovieModel
import com.example.cinephile.repository.MovieRepository

class MovieViewModel(val repository: MovieRepository) {


    fun addMovies(movieModel: MovieModel,
                  callback:(Boolean,String)->Unit){
        repository.addMovies(movieModel,callback)
    }

    fun updateMovie(movieId:String,
                    data:MutableMap<String,Any>,
                    callback: (Boolean, String) -> Unit){
        repository.updateMovie(movieId,data,callback)
    }

    fun deleteMovie(movieId:String,
                    callback: (Boolean, String) -> Unit){
        repository.deleteMovie(movieId,callback)

    }


    var _movies = MutableLiveData<MovieModel?>()
    var movies = MutableLiveData<MovieModel?>()

        get() = _movies

    var _allMovies = MutableLiveData<List<MovieModel>?>()
    var allMovies = MutableLiveData<List<MovieModel>?>()
        get() = _allMovies


    fun getMovieById(movieId: String){
        repository.getMovieById(movieId){
            movie,success,message->{
                if(success){
                    _movies.value = movie
                }
        }
        }
    }

    var _loadingState = MutableLiveData<Boolean>()
    var loadingState = MutableLiveData<Boolean>()

        get() = _loadingState

    fun getAllMovies(){
        _loadingState.value = true
        repository.getAllMovies(){
            movies,success,message->{
                if(success){
                    _allMovies.value = movies
                    _loadingState.value = false
                }
        }
        }

    }
}