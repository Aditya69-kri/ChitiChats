package com.example.chitichats.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.chitichats.R
import com.example.chitichats.data.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private lateinit var edtEmail:EditText
    private lateinit var edtPassword:EditText
    private lateinit var btnLogin:Button
    private lateinit var btnSignup:Button
    private lateinit var auth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()

        if(auth.currentUser!=null){
            val intent=Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnLogin.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            login(email,password)
        }
        btnSignup.setOnClickListener {
            val email=edtEmail.text.toString()
            val password=edtPassword.text.toString()
            signup(email,password)
        }
    }

    private fun init(){
        edtEmail=findViewById(R.id.edt_email)
        edtPassword=findViewById(R.id.edt_password)
        btnLogin=findViewById(R.id.btn_login)
        btnSignup=findViewById(R.id.btn_signup)
        auth= Firebase.auth
    }

    @SuppressLint("SuspiciousIndentation")
    private fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                val intent=Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // when user press back button, login activity will not be opened
                } else {
                  Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()
                }
            }
    }


    private fun signup(email:String,password:String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val listOfFollowings= mutableListOf<String>()
                    listOfFollowings.add("")
                    val listOfTweets= mutableListOf<String>()
                    listOfTweets.add("")
                    val user=User(
                        userEmail = email,
                        userImage = "",
                        listOfFollowings =listOfFollowings,
                        listOfTweets = listOfTweets,
                        uid = auth.uid.toString()
                    )
                    addUserToDatabase(user)
                    val intent=Intent(this, HomeActivity::class.java)
                    startActivity(intent)
                    finish() // when user press back button, login activity will not be opened
                } else {
                    Toast.makeText(this,"Something went wrong!",Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(user:User){
        Firebase.database.getReference("users").child(user.uid).setValue(user)
    }
}