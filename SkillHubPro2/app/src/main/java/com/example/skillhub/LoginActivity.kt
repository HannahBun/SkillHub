package com.example.skillhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val db = DBHelper(this)

        val username: EditText = findViewById(R.id.etUsername)
        val password: EditText = findViewById(R.id.etPassword)
        val loginBtn: Button = findViewById(R.id.btnLoginSubmit)
        val registerBtn: Button = findViewById(R.id.tvGoToRegister)

        loginBtn.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            val role = db.loginUser(user, pass)
            if (role == "learner") {
                val intent = Intent(this, LearnerDashboardActivity::class.java)
                intent.putExtra("username", user) // Pass username
                startActivity(intent)
            } else if (role == "instructor") {
                val intent = Intent(this, InstructorDashboardActivity::class.java)
                intent.putExtra("username", user) // Optional if needed for instructors
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid login", Toast.LENGTH_SHORT).show()
            }

        }

        registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
