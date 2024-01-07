package com.project.dowe

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.project.dowe.MainActivity.Companion.REQUEST_LOGIN
import com.project.dowe.MainActivity.Companion.REQUEST_LOGIN_FROM_ICON_LAPOR

class Login : AppCompatActivity() {
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var loginGoogle: AppCompatButton? = null
    private var mAuth: FirebaseAuth? = null
    private var mGoogleApiClient: GoogleApiClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        loginGoogle = findViewById<AppCompatButton>(R.id.login_google)
        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleApiClient = GoogleApiClient.Builder(this).enableAutoManage(
            this
        ) {
            Toast.makeText(
                applicationContext,
                "Koneksi dengan akun google gagal!!",
                Toast.LENGTH_SHORT
            ).show()
        }.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
        loginGoogle?.setOnClickListener(View.OnClickListener { loginAkunGoogle() })
    }

    private fun loginAkunGoogle() {
        mGoogleSignInClient?.signOut()?.addOnCompleteListener(this) {
            val intent = mGoogleSignInClient?.signInIntent
            if (intent != null) {
                startActivityForResult(intent, RC_SIGN_IN)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data!!)
            if (result!!.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        } else if (requestCode == REQUEST_LOGIN && resultCode == RESULT_OK) {
            // Tujuan setelah login dengan tombol login di MainActivity
            navigateToMainActivity()
        } else if (requestCode == REQUEST_LOGIN_FROM_ICON_LAPOR && resultCode == RESULT_OK) {
            // Tujuan setelah login dengan icon lapor di MainActivity
            navigateToLapor()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val user = mAuth!!.currentUser
                // Tambahkan pengecekan login sebelum pindah ke halaman Lapor
                // Jika sudah login, simpan status login dan pindah ke halaman Lapor
                saveLoginStatus(true)
                navigateToDestination()
                if (user != null) {
                    Toast.makeText(
                        applicationContext,
                        "Selamat Datang " + user.displayName,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                // Tambahkan logging untuk melihat pesan kesalahan
                Log.d("TAG", "Login dengan Google gagal: ${task.exception}")
                // Tambahkan tindakan lain sesuai kebutuhan, misalnya menampilkan pesan kesalahan
                Toast.makeText(
                    applicationContext,
                    "Login gagal, silakan coba lagi",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveLoginStatus(isLoggedIn: Boolean) {
        val sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.apply()
    }

    private fun navigateToDestination() {
        val intent: Intent
        if (isLoginFromMainActivity()) {
            intent = Intent(this, MainActivity::class.java)
        } else {
            intent = Intent(this, Lapor::class.java)
        }
        startActivity(intent)
        finish()
    }

    private fun isLoginFromMainActivity(): Boolean {
        val extras = intent.extras
        return extras != null && extras.containsKey(MainActivity.REQUEST_LOGIN.toString())
    }

    private fun navigateToMainActivity() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToLapor() {
        val intent = Intent(applicationContext, Lapor::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}
