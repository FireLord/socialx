package com.firelord.socialx

import android.os.Bundle
import android.view.*
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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
        setHasOptionsMenu(true)
        // init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu,inflater: MenuInflater) {
        inflater.inflate(R.menu.logout, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                firebaseAuth.signOut()
                view?.findNavController()?.navigate(R.id.action_newsHomeFragment_to_homeFragment)
            }
        }
        return true
    }
}