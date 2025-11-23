package com.example.skillhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val db = DBHelper(this)

        val username: EditText = findViewById(R.id.etRegUsername)
        val password: EditText = findViewById(R.id.etRegPassword)
        val learnerRadio: RadioButton = findViewById(R.id.rbLearner)
        val instructorRadio: RadioButton = findViewById(R.id.rbInstructor)
        val registerBtn: Button = findViewById(R.id.btnRegisterSubmit)
        val GoToLoginBtn: Button = findViewById(R.id.tvGoToLogin)

        registerBtn.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val role = if (learnerRadio.isChecked) "learner" else "instructor"

            val success = db.registerUser(user, pass, role)
            if (success) {
                Toast.makeText(this, "Registered!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, LoginActivity::class.java))
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show()
            }
        }

        GoToLoginBtn.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}