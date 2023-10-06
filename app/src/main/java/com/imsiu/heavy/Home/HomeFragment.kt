package com.imsiu.heavy.Home

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.google.firebase.firestore.GeoPoint
import com.imsiu.heavy.Helper.ConstantUID
import com.imsiu.heavy.Helper.FireBaseViewxModel
import com.imsiu.heavy.JSON.ClientModel
import com.imsiu.heavy.JSON.TruckModel
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() , TrucksAdapter.ClickListner{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentHomeBinding
    lateinit var fireBaseViewxModel : FireBaseViewxModel
    lateinit var adapter: TrucksAdapter
    lateinit var viewModel : HomeViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).showBottomNavigation(true) // Show/Hide Toolbar
        //      Inisiate Variables
        adapter = TrucksAdapter(this)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        fireBaseViewxModel = FireBaseViewxModel(requireContext())
        viewModel = HomeViewModel(Application())

        setUpAdapter(true)
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

    override fun addCollege(truck: TruckModel) {
        TODO("Not yet implemented")
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

}

//var clientModel: ClientModel? = null
//GlobalScope.launch(Dispatchers.IO) {
//    clientModel = fireBaseViewxModel.fetchUserInfo(ConstantUID)
//    Log.d("TAG!@#", "$clientModel") // You can log it here
//    Log.d("TAG!@#", "ConstantUID: $ConstantUID")
//}