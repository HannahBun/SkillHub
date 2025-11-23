package com.example.skillhub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LearnerDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learner_dashboard)

        val username = intent.getStringExtra("username") ?: ""

        val btnBrowseCourses: Button = findViewById(R.id.btnBrowseCourses)
        val btnMyEnrolled: Button = findViewById(R.id.btnMyCourses)
        val btnLogout: Button = findViewById(R.id.btnLogoutLearner)

        // Browse available courses
        btnBrowseCourses.setOnClickListener {
            val intent = Intent(this, CourseListActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        // View enrolled courses (FIXED)
        btnMyEnrolled.setOnClickListener {
            val intent = Intent(this, LearnerEnrolledCoursesActivity::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
