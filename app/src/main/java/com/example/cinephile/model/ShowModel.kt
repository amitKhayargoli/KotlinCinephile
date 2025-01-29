package com.example.cinephile.model

import android.os.Parcel
import android.os.Parcelable

class ShowModel(
    var showId: String = "",
    var showName: String = "",
    var showYear: String = "",
    var showSeasons: Int = 0,
    var showEpisodes: Int = 0,
    var showSummary: String = "",
    var IMDB: String = "",
    var imageUrl: String? = "",
    var imageName: String? = "",
    var leadActorUrl: String? = "",
    var leadActressUrl: String? = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(showId)
        parcel.writeString(showName)
        parcel.writeString(showYear)
        parcel.writeInt(showSeasons)
        parcel.writeInt(showEpisodes)
        parcel.writeString(showSummary)
        parcel.writeString(IMDB)
        parcel.writeString(imageUrl)
        parcel.writeString(imageName)
        parcel.writeString(leadActorUrl)
        parcel.writeString(leadActressUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShowModel> {
        override fun createFromParcel(parcel: Parcel): ShowModel {
            return ShowModel(parcel)
        }

        override fun newArray(size: Int): Array<ShowModel?> {
            return arrayOfNulls(size)
        }
    }
}
