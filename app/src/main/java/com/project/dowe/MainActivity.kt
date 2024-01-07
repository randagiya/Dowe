package com.project.dowe

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

class MainActivity : AppCompatActivity() {
    private lateinit var logoutButton: ImageButton
    private lateinit var loginButton: ImageButton
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        setContentView(R.layout.dashboard_layout)

        logoutButton = findViewById(R.id.logoutButton)
        loginButton = findViewById(R.id.loginButton)
        auth = FirebaseAuth.getInstance()

        updateUI()

        logoutButton.setOnClickListener {
            // Membuat objek AlertDialog
            val builder = AlertDialog.Builder(this)

            // Menentukan judul dan pesan dalam AlertDialog
            builder.setTitle("Konfirmasi Logout")
            builder.setMessage("Apakah Anda yakin ingin logout?")

            // Menambahkan tombol positif untuk konfirmasi logout
            builder.setPositiveButton("Logout") { dialog, which ->
                // Proses logout di sini
                auth.signOut()
                updateUI()
                clearLoginStatus()
                navigateToMainActivity()
            }

            // Menambahkan tombol negatif untuk membatalkan logout
            builder.setNegativeButton("Batal") { dialog, which ->
                // Membiarkan dialog ditutup tanpa melakukan apa pun
                dialog.dismiss()
            }

            // Membuat dan menampilkan AlertDialog
            val alertDialog: AlertDialog = builder.create()
            alertDialog.show()
        }

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
            handleIconLaporClick()
        }

        loginButton.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            startActivityForResult(intent, REQUEST_LOGIN)
        }

        // Berita
        val imageView = findViewById<ImageView>(R.id.imageView)
        val textView = findViewById<TextView>(R.id.textView)
        val button = findViewById<Button>(R.id.button)

        // Set image
        val url = "https://regional.kompas.com/read/2024/01/02/132006278/banjir-di-rokan-hulu-belum-surut-warga-beraktivitas-pakai-perahu"
        Glide.with(this)
            .load(url)
            .into(imageView)

        // Set text
        val doc = Jsoup.connect(url).get()
        val title = doc.select("title").text()
        textView.text = title

        // Set button
        button.text = "Baca Selengkapnya"
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_LOGIN, REQUEST_LOGIN_FROM_ICON_LAPOR -> handleLoginResult(requestCode, resultCode)
        }
    }

    private fun updateUI() {
        if (isLoggedIn()) {
            loginButton.visibility = View.GONE
            logoutButton.visibility = View.VISIBLE
        } else {
            loginButton.visibility = View.VISIBLE
            logoutButton.visibility = View.GONE
        }
    }

    private fun isLoggedIn(): Boolean {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun signOutAndNavigateToMainActivity() {
        auth.signOut()
        updateUI()
        clearLoginStatus()
        startActivity(createIntent(this))
        finish()
    }

    private fun clearLoginStatus() {
        getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
            .edit()
            .clear()
            .apply()
    }

    private fun handleIconLaporClick() {
        if (isLoggedIn()) {
            navigateToLapor()
        } else {
            val intent = Intent(this, Login::class.java)
            startActivityForResult(intent, REQUEST_LOGIN_FROM_ICON_LAPOR)
        }
    }

    private fun handleLoginResult(requestCode: Int, resultCode: Int) {
        when (resultCode) {
            RESULT_OK -> {
                if (requestCode == REQUEST_LOGIN) {
                    navigateToMainActivity()
                } else if (requestCode == REQUEST_LOGIN_FROM_ICON_LAPOR) {
                    navigateToLapor()
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(createIntent(this))
        finish()
    }

    private fun navigateToLapor() {
        val intent = Intent(this, Lapor::class.java)
        startActivity(intent)
    }

    companion object {
        const val REQUEST_LOGIN = 1
        const val REQUEST_LOGIN_FROM_ICON_LAPOR = 2

        fun createIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }
}
