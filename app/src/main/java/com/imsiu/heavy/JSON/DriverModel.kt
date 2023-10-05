package com.imsiu.heavy.JSON

import java.io.Serializable

data class DriverModel(
    var id : String ,
    var name : String,
    var email :String,
    var phoneNumb :String,
    var password :String,
    var price : Int,
//    var lat:Double,
//    var long :Double

): Serializable
