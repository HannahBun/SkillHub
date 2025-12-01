package com.example.skillhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*

class User : AppCompatActivity() {
    private lateinit var db: DBHelper
    private var mode: String = "login" //login mode by default
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper(this)

        //determine login or register
        mode = intent.getStringExtra("mode") ?: "login"
        if(mode == "login"){
            setContentView(R.layout.activity_login)
            setupLogin()
        }
        else{
            setContentView(R.layout.activity_register)
            setupRegister()
        }
    }

    //login
    private fun setupLogin(){
        val username: EditText = findViewById(R.id.etUsername)
        val password: EditText = findViewById(R.id.etPassword)
        val loginBtn: Button = findViewById(R.id.btnLoginSubmit)
        val registerBtn: Button = findViewById(R.id.tvGoToRegister)

        loginBtn.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            val role = db.loginUser(user, pass)
            when (role) {
                "learner" -> {
                    val intent = Intent(this, Learner::class.java)
                    intent.putExtra("username", user)
                    intent.putExtra("mode", "dashboard")
                    startActivity(intent)
                }
                "instructor" -> {
                    val intent = Intent(this, Instructor::class.java)
                    intent.putExtra("username", user)
                    startActivity(intent)
                }
                else -> {
                    Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
                }
            }
        }

        registerBtn.setOnClickListener {
            val intent = Intent(this, User::class.java)
            intent.putExtra("mode", "register")
            startActivity(intent)
        }
    }

    //register
    private fun setupRegister() {
        val username: EditText = findViewById(R.id.etRegUsername)
        val password: EditText = findViewById(R.id.etRegPassword)
        val learnerRadio: RadioButton = findViewById(R.id.rbLearner)
        val instructorRadio: RadioButton = findViewById(R.id.rbInstructor)
        val registerBtn: Button = findViewById(R.id.btnRegisterSubmit)
        val loginBtn: Button = findViewById(R.id.tvGoToLogin)

        registerBtn.setOnClickListener{
            val user = username.text.toString()
            val pass = password.text.toString()
            val role = if (learnerRadio.isChecked) "learner" else "instructor"

            val success = db.registerUser(user, pass, role)
            if (success){
                Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, User::class.java)
                intent.putExtra("mode", "login")
                startActivity(intent)
            }
            else{
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
        }
        loginBtn.setOnClickListener{
            val intent = Intent(this, User::class.java)
            intent.putExtra("mode", "login")
            startActivity(intent)
        }
    }
}