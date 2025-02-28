package com.example.cinephile.repository

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import com.example.cinephile.model.MovieModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.util.Executors
import java.io.InputStream

class MovieRepositoryImpl:MovieRepository {

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()

    val reference : DatabaseReference = database.reference.child("Movies")


    override fun addMovies(movieModel: MovieModel, callback: (Boolean, String) -> Unit) {
        var id = reference.push().key.toString()
        movieModel.movieId = id

        reference.child(id).setValue(movieModel).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }

        }
    }

    override fun updateMovie(
        movieId: String,
        data: MutableMap<String, Any>,
        callback: (Boolean, String) -> Unit
    ) {
        reference.child(movieId).updateChildren(data).addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }
        }
    }

    override fun deleteMovie(movieId: String, callback: (Boolean, String) -> Unit) {
        reference.child(movieId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                callback(true,"Product Added Successfully")

            }else{
                callback(false,"${it.exception?.message}")
            }

        }
    }

    override fun getMovieById(movieId: String, callback: (MovieModel?, Boolean, String) -> Unit) {
        reference.child(movieId).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var model = snapshot.getValue(MovieModel::class.java)

                    callback(model,true,"Data fetched")
                }
            }

            override fun onCancelled(error: DatabaseError) {

                callback(null,false,error.message.toString())
            }

        })



    }

    override fun getAllMovies(callback: (List<MovieModel>?, Boolean, String) -> Unit) {
        reference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var movies = mutableListOf<MovieModel>();

                if(snapshot.exists()){
                    for(movie in snapshot.children){
                        var model = movie.getValue(MovieModel::class.java)

                        if(model!=null){
                            movies.add(model)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                callback(null,false,error.message.toString())

            }

        })
    }


    }
