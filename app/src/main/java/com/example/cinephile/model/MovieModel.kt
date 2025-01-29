package com.example.cinephile.model

import android.os.Parcel
import android.os.Parcelable

class MovieModel
    (
    var movieId : String = "",
    var movieName : String = "",
    var movieYear : String = "",
    var movieRuntime : String = "",
    var movieSummary : String = "",
    var IMDB: String = "",
    var imageUrl : String? = "",
    var imageName : String? = "",
     var ActorUrl : String? = "",
     var ActressUrl : String? = "",
    ):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(movieId)
        parcel.writeString(movieName)
        parcel.writeString(movieYear)
        parcel.writeString(movieRuntime)
        parcel.writeString(movieSummary)
        parcel.writeString(IMDB)
        parcel.writeString(imageUrl)
        parcel.writeString(imageName)
        parcel.writeString(ActorUrl)
        parcel.writeString(ActressUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MovieModel> {
        override fun createFromParcel(parcel: Parcel): MovieModel {
            return MovieModel(parcel)
        }

        override fun newArray(size: Int): Array<MovieModel?> {
            return arrayOfNulls(size)
        }
    }
}
