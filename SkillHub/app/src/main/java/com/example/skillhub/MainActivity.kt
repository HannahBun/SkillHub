package com.example.skillhub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnLogin: Button = findViewById(R.id.btnLogin)
        val btnRegister: Button = findViewById(R.id.btnRegister)

        btnLogin.setOnClickListener {
            val intent = Intent(this, User::class.java)
            intent.putExtra("mode", "login")
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this, User::class.java)
            intent.putExtra("mode", "register")
            startActivity(intent)
        }

    }
}
