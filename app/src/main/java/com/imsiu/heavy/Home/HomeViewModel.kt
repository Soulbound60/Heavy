package com.imsiu.heavy.Home

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.JSON.TruckModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    var db = Firebase.firestore
    var trucksArray: ArrayList<TruckModel> = ArrayList()
    val trucksLive = MutableLiveData<List<TruckModel>>()



    fun getTrucks() {
        db.collection("Trucks")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val documentData = document.data
                    val truck = TruckModel(
                        documentData.get("id").toString(),
                        documentData.get("model").toString(),
                        documentData.get("rentPrice").toString(),
                        documentData.get("ownerUID").toString(),
                        documentData.get("city").toString(),
                        documentData.get("location")as GeoPoint,
                    )
                    trucksArray.add(truck)
                }
                trucksLive.value = trucksArray
            }
            .addOnFailureListener { exception ->
                // Handle the error here
            }
    }

    fun haversineDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        // Earth radius in kilometers
        val R = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Distance in kilometers
        val distance = R * c

        // Return rounded distance to two decimal places
        return String.format("%.2f", distance).toDouble()
    }

}