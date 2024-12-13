package com.example.aiventure.category

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.aiventure.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class CategoryFragment : Fragment() {

    private lateinit var tvYourLocation: TextView
    private lateinit var tvLocation: TextView
    private lateinit var ivArrow: View
    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager2: ViewPager2
    private lateinit var btnAll: Button
    private lateinit var btnJakarta: Button
    private lateinit var btnBandung: Button
    private lateinit var btnSemarang: Button
    private lateinit var btnYogyakarta: Button
    private lateinit var btnSurabaya: Button

    private var activePosition = 0
    private var activeCity = "All"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvYourLocation = view.findViewById(R.id.tvYourLocation)
        tvLocation = view.findViewById(R.id.tvLocation)
        ivArrow = view.findViewById(R.id.ivArrow)
        tabLayout = view.findViewById(R.id.tabLayout)
        viewPager2 = view.findViewById(R.id.viewPager2)
        btnAll = view.findViewById(R.id.btnAll)
        btnJakarta = view.findViewById(R.id.btnJakarta)
        btnBandung = view.findViewById(R.id.btnBandung)
        btnSemarang = view.findViewById(R.id.btnSemarang)
        btnSurabaya = view.findViewById(R.id.btnSurabaya)
        btnYogyakarta = view.findViewById(R.id.btnYogyakarta)

        setupViewPagerWithTabs()

        btnAll.setOnClickListener {
//            placeAdapter.submitList(this.places.toMutableList())
            activeCity = "All"
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshCurrentTab()
        }

        btnJakarta.setOnClickListener {
            activeCity = "Jakarta"
//            placeAdapter.submitList(this.places.toMutableList().filter { it.city == "Jakarta" })
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshCurrentTab()
        }

        btnSemarang.setOnClickListener {
            activeCity = "Semarang"
//            placeAdapter.submitList(this.places.toMutableList().filter { it.city == "Semarang" })
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshCurrentTab()
        }

        btnBandung.setOnClickListener {
            activeCity = "Bandung"
//            placeAdapter.submitList(this.places.toMutableList().filter { it.city == "Bandung" })
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshCurrentTab()
        }

        btnSurabaya.setOnClickListener {
            activeCity = "Surabaya"
//            placeAdapter.submitList(this.places.toMutableList().filter { it.city == "Surabaya" })
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            refreshCurrentTab()
        }

        btnYogyakarta.setOnClickListener {
            activeCity = "Yogyakarta"
//            placeAdapter.submitList(this.places.toMutableList().filter { it.city == "Yogyakarta" })
            btnAll.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnJakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSemarang.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnBandung.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnSurabaya.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#CCCCCC"))
            btnYogyakarta.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            refreshCurrentTab()
        }
    }

    private fun setupViewPagerWithTabs() {
        val adapter = CategoryPagerAdapter(requireActivity()) { position ->
            activePosition = position
            when (position) {
                0 -> {
                    DestinationFragment.newInstance(activeCity)
                }

                1 -> {
                    HotelFragment.newInstance(activeCity)
                }

                else -> {
                    RestaurantFragment.newInstance(activeCity)
                }
            }
        }
        viewPager2.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Destination"
                1 -> "Hotel"
                else -> "Restaurant"
            }
        }.attach()

        tabLayout.getTabAt(0)?.select()
    }

    private fun refreshCurrentTab() {
        val adapter = viewPager2.adapter as? CategoryPagerAdapter
        val currentFragment = adapter?.getFragment(viewPager2.currentItem)
        if (currentFragment is DestinationFragment) {
            currentFragment.updateCity(activeCity)
        } else if (currentFragment is HotelFragment) {
            currentFragment.updateCity(activeCity)
        } else if (currentFragment is RestaurantFragment) {
            currentFragment.updateCity(activeCity)
        }
    }
}
