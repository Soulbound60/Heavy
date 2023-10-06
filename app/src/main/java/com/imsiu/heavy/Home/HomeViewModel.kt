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
                        documentData.get("owner").toString(),
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
}