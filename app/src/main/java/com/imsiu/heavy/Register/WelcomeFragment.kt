package com.imsiu.heavy.Register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.imsiu.heavy.MainActivity
import com.imsiu.heavy.R
import com.imsiu.heavy.databinding.FragmentWelcomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WelcomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WelcomeFragment : Fragment() {
    lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        binding = FragmentWelcomeBinding.inflate(layoutInflater)


        binding.welcomeFragment.setOnClickListener {
            loadFragment(LogInFragment())
        }


        (activity as MainActivity).showBottomNavigation(false)
        //loadFragment(SignUpFragment())
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


}