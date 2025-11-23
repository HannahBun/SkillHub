package com.example.skillhub

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PostCourseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_course)

        // Enable back arrow in action bar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val db = DBHelper(this)

        // Get instructor username & ID
        val username = intent.getStringExtra("username") ?: ""
        val instructorId = db.getUserId(username) ?: -1

        val titleInput: EditText = findViewById(R.id.etCourseTitle)
        val descInput: EditText = findViewById(R.id.etCourseDescription)
        val priceInput: EditText = findViewById(R.id.etCoursePrice)
        val postBtn: Button = findViewById(R.id.btnPostCourseSubmit)
        val backBtn: Button = findViewById(R.id.btnBackToDashboard)

        // Post course
        postBtn.setOnClickListener {
            val title = titleInput.text.toString().trim()
            val desc = descInput.text.toString().trim()
            val price = priceInput.text.toString().toDoubleOrNull() ?: 0.0

            if (title.isEmpty() || desc.isEmpty()) {
                Toast.makeText(this, "Title and Description cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            db.addCourse(title, desc, price, instructorId)
            Toast.makeText(this, "Course Posted!", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Back button
        backBtn.setOnClickListener {
            finish()
        }
    }

    // Action bar back arrow
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
