package com.imsiu.heavy.Register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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
private lateinit var binding : FragmentLogInBinding
    val db = Firebase.firestore
    lateinit var fireBaseViewxModel : FireBaseViewxModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showBottomNavigation(false)
        binding = FragmentLogInBinding.inflate(layoutInflater)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
       // var fireBaseViewModel : FireBaseViewModel = FireBaseViewModel(requireContext())
        binding.logBtn.setOnClickListener {
            if (binding.emailTxt.text.isNotEmpty() && binding.passTxt.text.isNotEmpty()) {
//                userCheck(binding.emailTxt.text.toString(), binding.passTxt.text.toString())
                db.collection("Users")
                    .whereEqualTo("email", binding.emailTxt.text.toString())
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val documentData = document.data
                            Log.d("TAG", "${document.id} => ${document.data}")
                            val fbPassword = documentData.get("password").toString()
                            if (binding.passTxt.text.toString() == fbPassword) {
//
//                                val intent = Intent(requireActivity(),MainActivity::class.java)
//                                startActivity(intent)
//                                loadFragment(HomeFragment())
                            } else {
                                Toast.makeText(
                                    requireContext(),
                                    "الرقم او الاسم غير صحيح",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        fireBaseViewxModel.loginUser(binding.emailTxt.text.toString(),binding.passTxt.text.toString())
                    }
                    .addOnFailureListener { exception ->
                        Log.w("TAG", "Error getting documents.", exception)
                        Toast.makeText(
                            requireContext(),
                            "الرقم او الاسم غير صحيح",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

            } else {
                Toast.makeText(requireContext(), "الرجاء ادخال الرقم السري", Toast.LENGTH_SHORT)
                    .show()
            }

        }
        binding.signUpBtn.setOnClickListener {
            fireBaseViewxModel.loginUser("Soulbound60@gmail.com","Soulbound1122##")
           // loadFragment(HomeFragment())
        }
        fireBaseViewxModel.loginState.observe(viewLifecycleOwner, Observer { isLoggedIn ->
            if (isLoggedIn) {
                Log.d("TAG!@#","LogingStatues :${isLoggedIn}")
                loadFragment(HomeFragment())
            }
        })






        // Inflate the layout for this fragment
        return (binding.root)
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