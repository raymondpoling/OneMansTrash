package org.mousehole.americanairline.onemanstrash.view.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.mousehole.americanairline.onemanstrash.view.AddOfferFragment
import org.mousehole.americanairline.onemanstrash.view.ShowListedItemsFragment
import org.mousehole.americanairline.onemanstrash.view.ShowWatchItemsFragment

class SelectViewAdapter(fm: FragmentManager) :
        FragmentPagerAdapter(fm,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val showListedItemsFragment = ShowListedItemsFragment()
    private val showWatchItemsFragment = ShowWatchItemsFragment()
    private val addOfferFragment = AddOfferFragment()

    private val options = arrayListOf(showListedItemsFragment,
            showWatchItemsFragment,
            addOfferFragment)

    override fun getCount(): Int = options.size

    override fun getItem(position: Int): Fragment {
        return options[position]
    }
}
