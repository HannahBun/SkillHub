package com.example.skillhub

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog


class CourseListActivity : AppCompatActivity() {

    private lateinit var db: DBHelper
    private lateinit var listView: ListView
    private lateinit var courses: List<Course>
    private lateinit var adapter: CourseAdapter
    private var currentUserId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_list)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = DBHelper(this)
        listView = findViewById(R.id.lvCourses)
        val searchInput: EditText = findViewById(R.id.etSearch)
        val backBtn: Button = findViewById(R.id.btnCourseBackToDashboard)

        // Get current user
        val currentUsername = intent.getStringExtra("username") ?: ""
        currentUserId = db.getUserId(currentUsername) ?: -1

        // Load courses
        courses = db.getAllCourses()
        adapter = CourseAdapter(this, courses, currentUserId, db)
        listView.adapter = adapter

        // Search filter
        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered = courses.filter { course ->
                    course.title.lowercase().contains(query) || course.description.lowercase().contains(query)
                }
                adapter.updateList(filtered)
            }
        })

        // Back button
        backBtn.setOnClickListener { finish() }
    }

    override fun onResume() {
        super.onResume()
        // Refresh courses to update enrolled buttons
        courses = db.getAllCourses()
        adapter.updateList(courses)
    }

    // Adapter
    class CourseAdapter(
        private val context: CourseListActivity,
        private var courseList: List<Course>,
        private val userId: Int,
        private val db: DBHelper
    ) : BaseAdapter() {

        override fun getCount(): Int = courseList.size
        override fun getItem(position: Int): Any = courseList[position]
        override fun getItemId(position: Int): Long = courseList[position].id.toLong()

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view: View = convertView ?: LayoutInflater.from(context)
                .inflate(R.layout.course_list_item, parent, false)

            val course = courseList[position]

            val tvTitle = view.findViewById<TextView>(R.id.tvCourseTitle)
            val tvDescription = view.findViewById<TextView>(R.id.tvCourseDescription)
            val tvPrice = view.findViewById<TextView>(R.id.tvCoursePrice)
            val btnEnroll = view.findViewById<Button>(R.id.btnEnroll)

            tvTitle.text = course.title
            tvDescription.text = course.description
            tvPrice.text = "$${String.format("%.2f", course.price)}"

            // Disable enroll button if already enrolled
            val enrolledCourses = db.getEnrolledCourses(userId)
            if (enrolledCourses.any { it.id == course.id }) {
                btnEnroll.isEnabled = false
                btnEnroll.text = "Enrolled"
            } else {
                btnEnroll.isEnabled = true
                btnEnroll.text = "Enroll"
            }

            // Enroll button action
            btnEnroll.setOnClickListener {
                val enrolled = db.getEnrolledCourses(userId)
                if (enrolled.any { it.id == course.id }) {
                    Toast.makeText(context, "Already enrolled", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = Intent(context, PaymentActivity::class.java)
                    intent.putExtra("username", context.intent.getStringExtra("username"))
                    intent.putExtra("courseId", course.id)
                    context.startActivity(intent)
                }
            }

            return view
        }

        fun updateList(newList: List<Course>) {
            courseList = newList
            notifyDataSetChanged()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
