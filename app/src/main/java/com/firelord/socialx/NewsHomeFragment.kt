package com.firelord.socialx

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.firelord.socialx.databinding.FragmentHomeBinding
import com.firelord.socialx.databinding.FragmentNewsHomeBinding
import com.google.firebase.auth.FirebaseAuth

class NewsHomeFragment : Fragment() {

    private lateinit var binding: FragmentNewsHomeBinding

    // init firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_home, container, false)

        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        // handle click, logout
        binding.btLogout.setOnClickListener {
            firebaseAuth.signOut()
            binding.tvLogout.text = "Logged out"
        }

        return binding.root
    }

}