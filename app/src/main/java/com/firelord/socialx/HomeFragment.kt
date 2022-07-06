package com.firelord.socialx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.firelord.socialx.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    //  firebaseauth
    private lateinit var firebaseAuth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=DataBindingUtil.inflate(inflater,R.layout.fragment_home, container, false)
        binding.btStart.setOnClickListener {
            firebaseAuth = FirebaseAuth.getInstance()
            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser != null){
                // user is already logged in
                it.findNavController().navigate(R.id.action_homeFragment_to_newsHomeFragment2)
            }
            else {
                it.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        return binding.root
    }

}