package com.example.skillhub

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.adapters.Course
import com.example.skillhub.adapters.InstructorCourseAdapter

class Instructor : AppCompatActivity() {

    private lateinit var db: DBHelper
    private var username: String = ""
    private var instructorId: Int = -1

    private lateinit var rv: RecyclerView
    private lateinit var adapter: InstructorCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = DBHelper(this)

        username = intent.getStringExtra("username") ?: ""
        instructorId = db.getUserId(username) ?: -1

        if(instructorId < 0){
            Toast.makeText(this, "Instructor not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setDashboardView()
    }

    private fun setDashboardView() {
        setContentView(R.layout.activity_instructor_dashboard)

        val btnPostCourse: Button = findViewById(R.id.btnPostCourse)
        val btnMyCourses: Button = findViewById(R.id.btnMyCourses)
        val btnLogout: Button = findViewById(R.id.btnLogoutInstructor)

        btnPostCourse.setOnClickListener {
            val intent = android.content.Intent(this, PostCourse::class.java)
            intent.putExtra("username", username)
            startActivity(intent)
        }

        btnMyCourses.setOnClickListener {
            setCourseListView()
        }

        btnLogout.setOnClickListener {
            val intent = android.content.Intent(this, User::class.java)
            intent.flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("mode", "login")
            startActivity(intent)
        }
    }

    private fun setCourseListView() {
        setContentView(R.layout.activity_instructor_course_list)

        rv = findViewById(R.id.rvInstructorCourses)
        rv.layoutManager = LinearLayoutManager(this)

        val list = db.getCoursesByInstructor(instructorId)
        adapter = InstructorCourseAdapter(list.toMutableList(),
            onEdit = { course -> editCourse(course) },
            onDelete = { course -> confirmDelete(course) }
        )
        rv.adapter = adapter

        findViewById<Button>(R.id.btnInstructorBackToDash).setOnClickListener {
            setDashboardView()
        }
    }

    private fun editCourse(course: Course) {
        val dialogView = layoutInflater.inflate(R.layout.item_instructor_course_edit_dialog, null)

        val titleInput = dialogView.findViewById<EditText>(R.id.etEditTitle)
        val descInput = dialogView.findViewById<EditText>(R.id.etEditDescription)
        val priceInput = dialogView.findViewById<EditText>(R.id.etEditPrice)

        titleInput.setText(course.title)
        descInput.setText(course.description)
        priceInput.setText(course.price.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Course")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val updated = db.updateCourse(
                    course.id,
                    titleInput.text.toString(),
                    descInput.text.toString(),
                    priceInput.text.toString().toDoubleOrNull() ?: course.price
                )
                if(updated){
                    Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                    setCourseListView()
                }
                else{
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun confirmDelete(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Delete course")
            .setMessage("Delete \"${course.title}\"?")
            .setPositiveButton("Delete") { _, _ ->
                if(db.deleteCourse(course.id)){
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()

                    val fresh = db.getCoursesByInstructor(instructorId)
                    adapter.updateList(fresh)
                }
                else{
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
