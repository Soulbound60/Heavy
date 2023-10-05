package com.imsiu.heavy.JSON

import com.google.firebase.firestore.GeoPoint

data class TruckModel(
    var id :String,
    var model : String,
    var rentPrice :String,
    var owner :String,
    var location : GeoPoint
)
