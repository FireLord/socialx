package com.firelord.socialx.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.firelord.socialx.R
import com.firelord.socialx.adapters.ViewPagerAdapter
import com.firelord.socialx.databinding.FragmentTabsBinding
import com.google.android.material.tabs.TabLayoutMediator

class TabHolderFragment : Fragment() {


    private lateinit var binding: FragmentTabsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tabs, container, false)

        val tabLayout = binding.tabLayout
        val viewPager2 = binding.viewPager2
        val adapter= ViewPagerAdapter(parentFragmentManager,lifecycle)

        viewPager2.adapter=adapter

        TabLayoutMediator(tabLayout,viewPager2){tab,postition->
            when(postition){
                0->{
                    tab.text="Login"
                }
                1->{
                    tab.text="Sign Up"
                }
            }
        }.attach()

        return binding.root
    }

}