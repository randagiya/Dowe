package com.project.dowe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class KontakDarurat : AppCompatActivity() {
    private lateinit var textPolis: TextView
    private lateinit var textDamkar: TextView
    private lateinit var textBasarnas: TextView
    private lateinit var textRS: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.kontak_layout)

        val spinIdPo = findViewById<ImageView>(R.id.tampilStr_polis)
        val spinIdDa = findViewById<ImageView>(R.id.tampilStr_Damkar)
        val spinIdBa = findViewById<ImageView>(R.id.tampilStr_Basarnas)
        val spinIdRS = findViewById<ImageView>(R.id.tampilStr_RS)
        val backArrowImg = findViewById<ImageView>(R.id.backArrowImg)

        backArrowImg.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        textPolis = findViewById(R.id.textNoPolisi)
        textDamkar = findViewById(R.id.textNoDamkar)
        textBasarnas = findViewById(R.id.textNoBasarnas)
        textRS = findViewById(R.id.textNoRS)



        hideTextViews()

        val nomorPolisiArray = resources.getStringArray(R.array.nomor_pol_pekan)
        val nomorRumahSakitArray = resources.getStringArray(R.array.nomor_rs_pekan)
        val nomorDamkarArray = resources.getStringArray(R.array.nomor_dam_pekan)
        val nomorBasarnasArray = resources.getStringArray(R.array.nomor_ban_pekan)

        spinIdPo.setOnClickListener {
            toggleText(textPolis, nomorPolisiArray)
        }
        spinIdDa.setOnClickListener {
            toggleText(textDamkar, nomorDamkarArray)
        }
        spinIdBa.setOnClickListener {
            toggleText(textBasarnas, nomorBasarnasArray)
        }
        spinIdRS.setOnClickListener {
            toggleText(textRS, nomorRumahSakitArray)
        }
    }

    private fun toggleText(textView: TextView, nomorArray: Array<String>) {
        if (textView.visibility == View.VISIBLE) {
            textView.visibility = View.GONE
        } else {
            textView.text = nomorArray.joinToString("\n")
            textView.visibility = View.VISIBLE
            hideOtherTextViews(textView)
        }
    }

    private fun hideOtherTextViews(selectedTextView: TextView) {
        val textViews = listOf(textPolis, textDamkar, textBasarnas, textRS)
        textViews.forEach { textView ->
            if (textView != selectedTextView && textView.visibility == View.VISIBLE) {
                textView.visibility = View.GONE
            }
        }
    }

    private fun hideTextViews() {
        textPolis.visibility = View.GONE
        textDamkar.visibility = View.GONE
        textBasarnas.visibility = View.GONE
        textRS.visibility = View.GONE
    }
}




