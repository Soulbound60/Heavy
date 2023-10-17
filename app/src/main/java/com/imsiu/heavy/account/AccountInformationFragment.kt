package com.imsiu.heavy.account

import ClientModel
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.imsiu.heavy.Helper.ConstantClient
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.Home.HomeFragment
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentAccountInformationBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AccountInformationFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AccountInformationFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentAccountInformationBinding
    lateinit var firebaseModel : FireBaseViewxModel

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
        firebaseModel = FireBaseViewxModel(requireContext())
        binding = FragmentAccountInformationBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        binding.backBtn.setOnClickListener {
            loadFragment(HomeFragment())
        }
        Log.d("TAGTAG,","Client is : $ConstantClient")
        fillXml(ConstantClient)


        return binding.root
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

    fun fillXml(user:ClientModel){
        binding.usernameTxt.text = user.name
        binding.cityTxt.text = user.city
        binding.emailTxt.text = user.email
    }
}