package com.firelord.socialx.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.firelord.socialx.fragments.LoginFragment
import com.firelord.socialx.fragments.SignupFragment

class ViewPagerAdapter(fragmentManager: FragmentManager,lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle)
{
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->{
                LoginFragment()
            }
            1->{
                SignupFragment()
            }
            else->{
                Fragment()
            }
        }
    }
}