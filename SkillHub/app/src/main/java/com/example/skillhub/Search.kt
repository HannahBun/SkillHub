package com.example.skillhub

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.skillhub.adapters.CourseAdapter

class Search : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var listView: ListView
    private lateinit var adapter: CourseAdapter
    private var courses: List<com.example.skillhub.adapters.Course> = listOf()
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = DBHelper(this)

        listView = findViewById(R.id.lvCourses)
        val searchInput: EditText = findViewById(R.id.etSearch)
        val backBtn: Button = findViewById(R.id.btnCourseBackToDashboard)

        val currentUsername = intent.getStringExtra("username") ?: ""
        currentUserId = db.getUserId(currentUsername) ?: -1

        courses = db.getAllCourses()
        adapter = CourseAdapter(this, courses, currentUserId, db)
        listView.adapter = adapter

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered = courses.filter { course ->
                    course.title.lowercase().contains(query) ||
                            course.description.lowercase().contains(query)
                }
                adapter.updateList(filtered)
            }
        })

        backBtn.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        courses = db.getAllCourses()
        adapter.updateList(courses)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
