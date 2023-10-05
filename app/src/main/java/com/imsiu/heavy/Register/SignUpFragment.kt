package com.imsiu.heavy.Register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.FragmentActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.imsiu.heavy.JSON.ClientModel
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentSignUpBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class SignUpFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentSignUpBinding
    val db = Firebase.firestore
    var isDriver: Boolean = false
    var ClientOrDriver: String = "Client"
    lateinit var fireBaseViewxModel : FireBaseViewxModel


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
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        (activity as MainActivity).showBottomNavigation(false)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
        loadFragment(LogInFragment())
        val switch: SwitchCompat = binding.switch1
        switch.setOnCheckedChangeListener { _, isChecked ->
            ClientOrDriver = if (isChecked) "Driver" else "Client"
            // Optionally, handle any other logic based on the isChecked value
        }
        binding.signUpBtn.setOnClickListener {
            if (areFieldsFilled()) {
                if (passwordsMatch()) {
                    db.collection("Users")
                        .whereEqualTo("email", binding.emailTxt.text.toString())
                        .get()
                        .addOnSuccessListener { result ->
                            if (result.isEmpty) {
                                addNewUser(
                                    binding.usernameTxt.text.toString(),
                                    binding.emailTxt.text.toString(),
                                    binding.pass1Txt.text.toString(),
                                    binding.idTxt.text.toString(),
                                    binding.phoneNumber.text.toString()
                                )
                            } else {
                                Toast.makeText(requireContext(), "المستخدم موجود مسبقا!", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Handle passwords not matching if needed
                }
            } else {
                Toast.makeText(requireContext(), "الرجاء ادخال السري", Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return binding.root

    }

    private fun addNewUser(userName: String, email: String, password: String, id : String,phone :String) {
        fireBaseViewxModel.createUser(ClientModel(id, userName,
            email,password,binding.phoneNumber.text.toString(),ClientOrDriver,24.740185, 46.703108))
        loadFragment(LogInFragment())

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

    private fun areFieldsFilled(): Boolean {
        return listOf(binding.idTxt, binding.emailTxt, binding.pass1Txt, binding.pass2Txt, binding.usernameTxt).all { it.text.isNotEmpty() }
    }
    private fun passwordsMatch(): Boolean {
        return binding.pass1Txt.text.toString() == binding.pass2Txt.text.toString()
    }

}