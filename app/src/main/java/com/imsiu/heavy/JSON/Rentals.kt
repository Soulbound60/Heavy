package com.imsiu.heavy.JSON

data class Rentals(
    var id :String,
    var model : String,
    var rentPrice :String,
    var owner :String,
    var isUsed : Boolean,
    var currentRental : String,
    var startDate :String,
    var endDate:String,
    var lat:Double,
    var long :Double

)
