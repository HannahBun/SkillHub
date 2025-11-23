package com.example.skillhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class InstructorDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_dashboard)

        val username = intent.getStringExtra("username") ?: ""

        val btnPostCourse: Button = findViewById(R.id.btnPostCourse)
        val btnMyCourses: Button = findViewById(R.id.btnMyCourses)
        val btnLogout: Button = findViewById(R.id.btnLogoutInstructor)

        // Post a new course
        btnPostCourse.setOnClickListener {
            val intent = Intent(this, PostCourseActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // View instructor’s posted courses
        btnMyCourses.setOnClickListener {
            val intent = Intent(this, InstructorCourseListActivity::class.java)
            intent.putExtra("username", username)

            intent.putExtra("username", username)
            startActivity(intent)
        }

        // Logout → back to main login
        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
