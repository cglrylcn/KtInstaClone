package com.caglar.ktinstaclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.caglar.ktinstaclone.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this,FeedActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signIn(view: View) {
        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(this,"E-mail and Password can not be empty.",Toast.LENGTH_LONG).show()
        }
        else {
            auth.signInWithEmailAndPassword(email,password)
                .addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
                .addOnSuccessListener {
                    Toast.makeText(this,"Login Success",Toast.LENGTH_LONG).show()
                    val intent = Intent(this,FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
        }
    }

    fun signUp(view: View) {
        email = binding.emailText.text.toString()
        password = binding.passwordText.text.toString()
        if (email == "" || password == "") {
            Toast.makeText(this,"E-mail and Password can not be empty.",Toast.LENGTH_LONG).show()
        }
        else {
            auth.createUserWithEmailAndPassword(email,password)
                .addOnSuccessListener {
                    //Sign up process completed
                    val intent = Intent(this,FeedActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    //Sign up process failed
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }
        }
    }

}