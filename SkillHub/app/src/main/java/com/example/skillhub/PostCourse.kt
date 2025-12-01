package com.example.skillhub

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PostCourse : AppCompatActivity() {

    private lateinit var db: DBHelper
    private var instructorId: Int = -1
    private lateinit var username: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_course)

        db = DBHelper(this)
        username = intent.getStringExtra("username") ?: ""
        instructorId = db.getUserId(username) ?: -1

        if(instructorId < 0){
            Toast.makeText(this, "Instructor not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etTitle = findViewById<EditText>(R.id.etCourseTitle)
        val etDesc = findViewById<EditText>(R.id.etCourseDescription)
        val etPrice = findViewById<EditText>(R.id.etCoursePrice)
        val btnPost = findViewById<Button>(R.id.btnPostCourseSubmit)

        btnPost.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc = etDesc.text.toString().trim()
            val price = etPrice.text.toString().toDoubleOrNull()

            if(title.isEmpty() || desc.isEmpty() || price == null){
                Toast.makeText(this, "Please fill all fields correctly", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = db.addCourse(title, desc, price, instructorId)
            if(success != -1L){
                Toast.makeText(this, "Course posted successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            else{
                Toast.makeText(this, "Failed to post course", Toast.LENGTH_SHORT).show()
            }
        }
        val btnBack = findViewById<Button>(R.id.btnBackToDashboard)

        btnBack.setOnClickListener {
            finish()
        }

    }
}
