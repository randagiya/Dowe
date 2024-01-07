package com.project.dowe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException


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
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(
                data!!
            )
            if (result!!.isSuccess) {
                val account = result.signInAccount
                firebaseAuthWithGoogle(account)
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount?) {
        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        mAuth!!.signInWithCredential(credential).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                val user = mAuth!!.currentUser
                val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Selamat Datang " + user!!.displayName,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(applicationContext, "Authentication Failed", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    companion object {
        private const val RC_SIGN_IN = 123
    }
}