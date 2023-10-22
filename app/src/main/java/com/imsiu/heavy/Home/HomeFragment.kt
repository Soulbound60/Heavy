package com.imsiu.heavy.Home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.imsiu.heavy.Helper.ConstantClient
import com.imsiu.heavy.Helper.ConstantTruck
import com.imsiu.heavy.Helper.ConstantUID
import com.imsiu.heavy.Helper.CurrentLocation
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.JSON.TruckModel
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.R
import com.imsiu.heavy.Register.SignUpFragment
import com.imsiu.heavy.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.*

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() , TrucksAdapter.ClickListner{
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentHomeBinding
    lateinit var fireBaseViewxModel : FireBaseViewxModel
    lateinit var adapter: TrucksAdapter
    lateinit var viewModel : HomeViewModel
  //  private lateinit var fusedLocationClient: FusedLocationProviderClient


    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showBottomNavigation(true) // Show/Hide Toolbar
        //      Inisiate Variables
       // fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        adapter = TrucksAdapter(this)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
        viewModel = HomeViewModel(Application())
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MODE_CHANGED)

    if (ConstantClient.type.toString()=="Client"){
        binding.fBtn.visibility = View.GONE
        Log.d("TEST!@#","it is a Client")
    }else{
        binding.fBtn.visibility = View.VISIBLE
        binding.fBtn.setOnClickListener {
            loadFragment(AddTruckFragment())
        }
    }
        var fragg = SignUpFragment()
        Log.d("FindingTheError","statues fragment Activity=${fragg.activity}")


        setUpAdapter(true)


        return binding.root
    }

    private fun loadFragment(fragment: Fragment) {
        // Ensure that the fragment is not null before proceeding
        if (fragment != null) {
            val bundle = Bundle()
            // If you want to pass data to the new fragment, you can do it here
            // bundle.putString("userId", userId)
            fragment.arguments = bundle

            // Use the correct FragmentManager instance
            val supportFragmentManager = requireActivity().supportFragmentManager

            // Begin the fragment transaction
            val transaction = supportFragmentManager.beginTransaction()

            // Replace the existing fragment in the R.id.frame_layout container
            transaction.replace(R.id.frame_layout, fragment)

            // Add the transaction to the back stack (optional but useful for navigation)
            transaction.addToBackStack(null)

            // Commit the transaction to apply the changes
            transaction.commit()
        }
    }



    override fun truckDetail(truck: TruckModel) {
        ConstantTruck = truck
        loadFragment(TruckDetailFragment())

    }

    override fun distanceIs(truck: TruckModel):Double {
        Log.d("TestDistance","Truck name :${truck.city} \n ${truck.location.latitude},${truck.location.longitude}" +
                "\n${CurrentLocation.latitude},${CurrentLocation.longitude}")
        return haversineDistance(
            truck.location.latitude,truck.location.longitude,
            CurrentLocation.latitude, CurrentLocation.longitude
        )

    }

    fun setUpAdapter(user : Boolean){
        if (user){
            if (user) {
                val rv = binding.homeRv
                rv.adapter = adapter
                viewModel.getTrucks()
                viewModel.trucksLive.observe(viewLifecycleOwner, Observer { trucksList ->
                    adapter.submitList(trucksList)
                })
            }
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
