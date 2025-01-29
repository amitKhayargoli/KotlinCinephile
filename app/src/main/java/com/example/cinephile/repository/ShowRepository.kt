package com.example.cinephile.repository

import com.example.cinephile.model.ShowModel

interface ShowRepository {

    fun addShows(showModel: ShowModel,
                  callback:(Boolean,String)->Unit)

    fun updateShow(showId:String,
                    data:MutableMap<String,Any>,
                    callback: (Boolean, String) -> Unit)

    fun deleteShow(showId:String,
                    callback: (Boolean, String) -> Unit)

    fun getShowById(showId: String,
                     callback: (ShowModel?,Boolean, String) -> Unit)

    fun getAllShows(callback: (List<ShowModel>?,Boolean, String) -> Unit)

//    fun getShowByid(callback: (List<ShowModel>?, Boolean, String) -> Unit)
}