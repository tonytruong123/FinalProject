package com.example.idea6.worker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.idea6.CustomDictFragment
import com.example.idea6.NewSettingsFragment
import com.example.idea6.word_of_the_day

class TabPageAdapter(activity: FragmentActivity, private val tabCount: Int) : FragmentStateAdapter(activity)
{
    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment
    {
        return when (position)
        {
            0 -> word_of_the_day()
            1 -> CustomDictFragment()
            2 -> NewSettingsFragment()
            else -> word_of_the_day()
        }
    }

}