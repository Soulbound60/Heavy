package com.imsiu.heavy.Home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.JSON.TruckModel
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentAddTruckBinding
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.Observer
import com.imsiu.heavy.Helper.ConstantUID
import com.imsiu.heavy.Helper.FireBaseViewxModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddTruckFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddTruckFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding : FragmentAddTruckBinding

    private lateinit var spinner: Spinner
    private lateinit var selectedItemTextView: TextView
    private lateinit var firebaseX:FireBaseViewxModel
    private var logedStatues : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        binding = FragmentAddTruckBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        Log.d("TAGTAG!","Selected Item: ${binding.selectedItemTextview.text.toString()}")
        spinner = binding.spinner
        selectedItemTextView = binding.selectedItemTextview
        firebaseX = FireBaseViewxModel(requireContext())
        Log.d("TAGUUID","it is = $ConstantUID")
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.cities,  // Replace with your own array resource
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                // Get the selected item from the spinner
                val selectedItem = parent.getItemAtPosition(position).toString()

                // Update the TextView to display the selected item
                selectedItemTextView.text = "Selected Item: $selectedItem"
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }


        binding.saveBtn.setOnClickListener {
            if (binding.nameTxt.text.isNotEmpty()&&binding.phoneNumber.text.isNotEmpty()&&binding.priceTxt.text.isNotEmpty()&&binding.modelTxt.text.isNotEmpty()&&binding.priceTxt.text.isNotEmpty()&&binding.registrationTxt.text.isNotEmpty()){
                GlobalScope.launch {
                    val fullText = binding.selectedItemTextview.text.toString()
                    val strippedText = fullText.removePrefix("Selected Item: ")
                    add(TruckModel(binding.registrationTxt.text.toString(),binding.modelTxt.text.toString(),binding.priceTxt.text.toString(),
                        ConstantUID,strippedText))
                }


            }else{
                Toast.makeText(requireContext(),"Pls fill all the Info", Toast.LENGTH_SHORT).show()
            }

        }

        return binding.root
    }
    suspend fun add(truck: TruckModel) {
        val client = firebaseX.fetchUserInfo(ConstantUID)

        if (client != null) {
            val updatedTruck = TruckModel(
                client.id,
                truck.model,
                truck.rentPrice,
                ConstantUID,
                truck.city.toString()
            )
            Log.d("TAGUUID", "it is = $ConstantUID")
            val db = Firebase.firestore
            db.collection("Trucks")
                .add(updatedTruck)
                .addOnSuccessListener { documentReference ->
                    Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w("TAG", "Error adding document", e)
                }
        } else {
            Log.d("TAGUUID", "Client information not available")
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val bundle = Bundle()
        var supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
        //bundle.putString("userId",userId)
        fragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}