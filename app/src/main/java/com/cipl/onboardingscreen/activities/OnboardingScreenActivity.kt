package com.cipl.onboardingscreen.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.cipl.onboardingscreen.R
import com.cipl.onboardingscreen.adapter.OnboardingViewPagerAdapter
import com.cipl.onboardingscreen.model.OnboardingData
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener

class OnboardingScreenActivity : AppCompatActivity() {

    private var onboardingViewPagerAdapter: OnboardingViewPagerAdapter? = null
    private var tabLayout: TabLayout? = null
    private var onboardingViewPager: ViewPager? = null
    var tvNext: TextView? = null
    var position = 0
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restorePrevData()) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Removing actionbar title and making it full screen in onboarding activity
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        supportActionBar!!.hide()

        setContentView(R.layout.activity_onboarding_screen)

        tabLayout = findViewById(R.id.tablayout)
        tvNext = findViewById(R.id.tvNext)

        val onboardingData: MutableList<OnboardingData> = ArrayList()
        onboardingData.add(
            OnboardingData(
                "Fresh Food",
                "Fresh food is always available at our store. Order now to get at your door step.",
                R.drawable.onboarding_image_1
            )
        )
        onboardingData.add(
            OnboardingData(
                "Fast Delivery",
                "We deliver foods as soon as customer place the order. Order now to see our delivery speed.",
                R.drawable.onboarding_image_2
            )
        )
        onboardingData.add(
            OnboardingData(
                "Easy Payment",
                "Pay online for all your orders. Making easy payments for our customer.",
                R.drawable.onboarding_image_3
            )
        )
        setOnboardingViewPagerAdapter(onboardingData)

        position = onboardingViewPager!!.currentItem

        tvNext?.setOnClickListener {
            if (position < onboardingData.size) {
                position++
                onboardingViewPager!!.currentItem = position
            }
            if (position == onboardingData.size) {
                savePrevData()
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

        }

        tabLayout!!.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                if (tab.position == onboardingData.size - 1) {
                    tvNext!!.text = "Get Started"
                } else {
                    tvNext!!.text = "Next"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

        })
    }

    private fun setOnboardingViewPagerAdapter(onboardingData: List<OnboardingData>) {
        onboardingViewPager = findViewById(R.id.viewpager)
        onboardingViewPagerAdapter = OnboardingViewPagerAdapter(this, onboardingData)
        onboardingViewPager!!.adapter = onboardingViewPagerAdapter      // Setting adapter in viewpager
        tabLayout?.setupWithViewPager(onboardingViewPager)
    }

    private fun savePrevData() {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrevData(): Boolean {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }
}