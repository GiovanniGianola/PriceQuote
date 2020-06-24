package com.example.pricequote.ui.invoice

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.example.pricequote.R
import com.example.pricequote.ui.invoice.fragment.CategoryFragment
import com.example.pricequote.ui.invoice.fragment.CustomOptionFragment
import com.example.pricequote.ui.invoice.fragment.InfoFragment
import com.example.pricequote.ui.invoice.fragment.SpaceFragment

private val TAB_TITLES = arrayOf(
    R.string.tab_text_1,
    R.string.tab_text_2,
    R.string.tab_text_3,
    R.string.tab_text_4
)

class SectionsPagerAdapter(private val context: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){

    private var pos = 0

    override fun getItem(position: Int): Fragment {
        pos = position
        return when(position){
            0 -> InfoFragment.newInstance()
            1 -> SpaceFragment.newInstance()
            2 -> CategoryFragment.newInstance()
            3 -> CustomOptionFragment.newInstance()
            else -> InfoFragment.newInstance()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    interface EventListener {
        fun saveLocalFragmentUpdates()
    }
}