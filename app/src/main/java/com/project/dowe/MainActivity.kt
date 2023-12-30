package com.project.dowe

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_layout)


        val iconBerita = findViewById<ImageView>(R.id.iconBerita)
        val iconTransport = findViewById<ImageView>(R.id.iconTransport)
        val iconDarurat = findViewById<ImageView>(R.id.iconDarurat)
        val iconLapor = findViewById<ImageView>(R.id.iconLapor)

        iconBerita.setOnClickListener {
            val intent = Intent(this, Berita::class.java)
            startActivity(intent)
        }
        iconTransport.setOnClickListener {
            val intent = Intent(this, Transport::class.java)
            startActivity(intent)
        }
        iconDarurat.setOnClickListener {
            val intent = Intent(this, KontakDarurat::class.java)
            startActivity(intent)
        }
        iconLapor.setOnClickListener {
            val intent = Intent(this, Lapor::class.java)
            startActivity(intent)
        }
    }


}