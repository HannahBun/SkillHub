package com.example.skillhub

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LearnerEnrolledCoursesActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var rv: RecyclerView
    private lateinit var adapter: EnrolledCourseAdapter
    private var userId: Int = -1
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learner_enrolled)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = DBHelper(this)
        rv = findViewById(R.id.rvEnrolledCourses)
        rv.layoutManager = LinearLayoutManager(this)

        username = intent.getStringExtra("username") ?: ""
        userId = db.getUserId(username) ?: -1

        val list = if (userId >= 0) db.getEnrolledCourses(userId) else listOf()
        adapter = EnrolledCourseAdapter(list)
        rv.adapter = adapter

        findViewById<android.widget.Button>(R.id.btnLearnerBackToDash).setOnClickListener { finish() }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
