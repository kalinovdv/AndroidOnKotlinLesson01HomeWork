package ru.geekbrains.mymenuauth.data

import android.os.Parcel
import android.os.Parcelable

import java.util.Date

class NoteData : Parcelable{
    var id : String
    var titel : String
    var text : String
    var date : Date

    constructor(titel : String, text : String, date : Date) {
        this.titel = titel
        this.text = text
        this.date = date
        this.id = ""
    }

    constructor(parsel : Parcel) {
        titel = parsel.readString()?:""
        text = parsel.readString()?:""
        date = Date(parsel.readLong())
        id = parsel.readString()?:""
    }

    companion object CREATOR : Parcelable.Creator<NoteData> {
        override fun createFromParcel(parsel : Parcel) : NoteData {
            return NoteData(parsel)
        }

        override fun newArray(size : Int) : Array<NoteData?> {
            return arrayOfNulls(size)
        }
    }

    override fun describeContents() : Int {
        return 0
    }

    override fun writeToParcel(dest : Parcel, flags : Int) {
        dest.writeString(titel)
        dest.writeString(text)
        dest.writeString(id)
        dest.writeLong(date.time)
    }

}
