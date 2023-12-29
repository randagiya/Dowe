package com.project.dowe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
class Transport : AppCompatActivity() {


    private lateinit var viewPager: ViewPager2
    private lateinit var sliderAdapter: SliderAdapter

    private val handler = android.os.Handler()
    private val delay = 3000L // Delay 3 detik
    private var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transport_layout)

        viewPager = findViewById(R.id.viewPager)

        val images = listOf(
            R.drawable.bus1,
            R.drawable.bus2,
            R.drawable.bus3
            // Tambahkan gambar lainnya di sini
        )

        sliderAdapter = SliderAdapter(images)
        viewPager.adapter = sliderAdapter

        // Mulai otomatisasi penggeseran slider
        startAutoSlider()
    }

    private val runnable = object : Runnable {
        override fun run() {
            if (currentPage == sliderAdapter.itemCount) {
                currentPage = 0
            }
            viewPager.setCurrentItem(currentPage++, true)
            handler.postDelayed(this, delay)
        }
    }

    private fun startAutoSlider() {
        handler.postDelayed(runnable, delay)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }
}