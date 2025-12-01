package com.example.skillhub

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.adapters.EnrolledCourseAdapter

class Learner : AppCompatActivity() {

    private lateinit var db: DBHelper
    private var username: String = ""
    private var userId: Int = -1

    private lateinit var rv: RecyclerView
    private lateinit var adapter: EnrolledCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper(this)

        username = intent.getStringExtra("username") ?: ""
        userId = db.getUserId(username) ?: -1

        if (userId < 0) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setDashboardView()
    }

    private fun setDashboardView() {
        setContentView(R.layout.activity_learner_dashboard)

        val btnBrowseCourses: Button = findViewById(R.id.btnBrowseCourses)
        val btnMyEnrolled: Button = findViewById(R.id.btnMyCourses)
        val btnLogout: Button = findViewById(R.id.btnLogoutLearner)

        btnBrowseCourses.setOnClickListener {
            val intent = android.content.Intent(this, Search::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        btnMyEnrolled.setOnClickListener {
            setEnrolledCoursesView()
        }

        btnLogout.setOnClickListener {
            val intent = android.content.Intent(this, User::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or
                    android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("mode", "login")
            startActivity(intent)
        }
    }

    private fun setEnrolledCoursesView() {
        setContentView(R.layout.activity_learner_enrolled)

        rv = findViewById(R.id.rvEnrolledCourses)
        rv.layoutManager = LinearLayoutManager(this)

        val list = db.getEnrolledCourses(userId)
        adapter = EnrolledCourseAdapter(list)
        rv.adapter = adapter

        findViewById<Button>(R.id.btnLearnerBackToDash).setOnClickListener {
            setDashboardView()
        }
    }
}
