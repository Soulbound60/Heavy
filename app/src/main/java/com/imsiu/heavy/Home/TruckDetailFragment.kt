package com.imsiu.heavy.Home

import ClientModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.firestore.GeoPoint
import com.imsiu.heavy.Helper.ConstantTruck
import com.imsiu.heavy.Helper.CurrentLocation
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.JSON.TruckModel
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentTruckDetailBinding
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

class TruckDetailFragment : Fragment() {
    lateinit var binding: FragmentTruckDetailBinding
    lateinit var firebaseModel : FireBaseViewxModel
    var truckOwner :ClientModel = ClientModel("dd","dd","dd","dd","dd","dd","dd")
    var flag : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }
var ownerPhoneNumber = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseModel = FireBaseViewxModel(requireContext())
        binding = FragmentTruckDetailBinding.inflate(layoutInflater)
        Log.d("TESTTAG","Here is the new Constant :\n $ConstantTruck")



        CoroutineScope(Dispatchers.IO).launch {
            Log.d("COMPLETE!"," 1courutine just started: $truckOwner")
            // First coroutine job
            val truckOwner = async {
                firebaseModel.fetchUserInfo(ConstantTruck.ownerUID)
            }

            // Wait for the first coroutine to complete
            val result = truckOwner.await()

            // Check if the first coroutine was successful
            if (result != null) {
                Log.d("COMPLETE!"," 2 $result")
                // Call another function here, for example:
                withContext(Dispatchers.Main) {
                    fillXML(result, ConstantTruck)
                }

            }
        }
        binding.fabAddSpare.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + truckOwner.phoneNumber))
            startActivity(intent)
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun fillXML(owner:ClientModel,truck :TruckModel){
    binding.truckModelTxt.text = truck.model
    binding.rentTxt.text = truck.rentPrice
    binding.ownerNameTxt.text = owner.name
    var distanceInKm = haversineDistance(truck.location, CurrentLocation)
    var priceBasedOnKM = distanceInKm * truck.rentPrice.toDouble()
    binding.priceBasedOnlocation.text = "'$priceBasedOnKM' SR"
    binding.distanceKMTxt.text = distanceInKm.toString() + "KM"
}

    fun haversineDistance(geo1:GeoPoint,geo2:GeoPoint
    ): Double {
        // Earth radius in kilometers
        val R = 6371.0

        val dLat = Math.toRadians(geo2.latitude - geo1.latitude)
        val dLon = Math.toRadians(geo2.longitude  - geo1.longitude)

        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(geo1.latitude)) * cos(Math.toRadians(geo2.latitude)) *
                sin(dLon / 2).pow(2.0)

        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        // Distance in kilometers
        val distance = R * c

        // Return rounded distance to two decimal places
        return String.format("%.2f", distance).toDouble()
    }
}