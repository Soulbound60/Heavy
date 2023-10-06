package com.imsiu.heavy.JSON

import com.google.firebase.firestore.GeoPoint

data class TruckModel(
    var id :String,
    var model : String,
    var rentPrice :String,
    var owner :String,
    var city : String,
    var location : GeoPoint = GeoPoint(23.23,23.12)
)
