package com.imsiu.heavy.Register

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.Helper.CurrentLocation
import com.imsiu.heavy.Home.HomeFragment
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentLogInBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LogInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LogInFragment : Fragment() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
private lateinit var binding : FragmentLogInBinding
    val db = Firebase.firestore
    lateinit var fireBaseViewxModel : FireBaseViewxModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showBottomNavigation(false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        binding = FragmentLogInBinding.inflate(layoutInflater)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
        fireBaseViewxModel.loginState.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn) {
                loadFragment(HomeFragment())
            }
        })
       // var fireBaseViewModel : FireBaseViewModel = FireBaseViewModel(requireContext())
        binding.logBtn.setOnClickListener {
            runnObservers()
            //binding.emailTxt.text.isNotEmpty() && binding.passTxt.text.isNotEmpty()
            if (true) {
                fireBaseViewxModel.loginUser("soulbound60@gmail.com","112233Qq")
            } else {
                Toast.makeText(requireContext(), "الرجاء ادخال الرقم السري", Toast.LENGTH_SHORT)
                    .show()
                binding.fpTxt.visibility = View.VISIBLE
            }
        }
        binding.signUpBtn.setOnClickListener {
            Log.d("FindingTheError","Beging of the Click")
            loadFragment(SignUpFragment())
        }

        binding.fpTxt.setOnClickListener {
        showResetPasswordDialog(requireContext())
    }




        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                Log.d("TAG!@#"," Location:  \nLat: ${it.latitude} \nLong= ${it.longitude}")
               CurrentLocation = GeoPoint(it.latitude,it.longitude)
            }
        }

        // Inflate the layout for this fragment
        return (binding.root)
    }

    private fun loadFragment(fragment: Fragment) {
        Log.d("FindingTheError","Beging of the loadFragment Function")
        val bundle = Bundle()
        var supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
        //bundle.putString("userId",userId)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        Log.d("FindingTheError","middle of the loadFragment Function")
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
        Log.d("FindingTheError","end of the loadFragment Function")


    }

    private fun runnObservers(){
        fireBaseViewxModel.loginState.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn) {
                loadFragment(HomeFragment())
            }
        })
    }


    fun showResetPasswordDialog(context: Context) {
        val alertDialog = AlertDialog.Builder(context)
        alertDialog.setTitle("Reset Password")
        alertDialog.setMessage("Enter your email address to reset your password")

        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Reset") { _, _ ->
            val email = input.text.toString()

            fireBaseViewxModel.forgetPassword(email)
            // Here, you can handle the email input and initiate the password reset process.
            // You might want to send an email with a reset link or follow your app's logic for password reset.
            // For demonstration purposes, we'll simply display a Toast message with the entered email.
            Toast.makeText(context, "Reset password for $email", Toast.LENGTH_SHORT).show()
        }

        alertDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        alertDialog.show()
    }




}