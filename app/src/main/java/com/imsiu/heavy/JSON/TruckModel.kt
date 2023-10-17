package com.imsiu.heavy.JSON

import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint
import java.io.Serializable


data class TruckModel(
    var id :String,
    var model : String,
    var rentPrice :String,
    var ownerUID :String,
    var city : String,
    var location : GeoPoint = GeoPoint(23.23,23.12)
): Serializable
