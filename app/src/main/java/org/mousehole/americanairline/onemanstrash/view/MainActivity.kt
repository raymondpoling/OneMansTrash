package org.mousehole.americanairline.onemanstrash.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.mousehole.americanairline.onemanstrash.R
import org.mousehole.americanairline.onemanstrash.utils.DebugLogger.debug
import org.mousehole.americanairline.onemanstrash.view.adapter.SelectViewAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // bindings
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        viewPager = findViewById(R.id.display_viewpager)

        viewPager.adapter = SelectViewAdapter(supportFragmentManager)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            debug("Got id: ${it.itemId} ?= ${R.id.list_all_items}")
            when (it.itemId) {
                R.id.list_all_items -> viewPager.currentItem = 0
                R.id.selling_items -> viewPager.currentItem = 2
                R.id.watch_items -> viewPager.currentItem = 1
            }
            true
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // nop
            }

            override fun onPageSelected(position: Int) {
                bottomNavigationView.selectedItemId = when (position) {
                    0 -> R.id.list_all_items
                    2 -> R.id.selling_items
                    1 -> R.id.watch_items
                    else -> R.id.list_all_items
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                // nop
            }

        })
    }
}
