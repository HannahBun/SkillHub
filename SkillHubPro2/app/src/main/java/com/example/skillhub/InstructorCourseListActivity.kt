package com.example.skillhub

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.InstructorCourseAdapter.OnCourseActionListener

class InstructorCourseListActivity : AppCompatActivity(), OnCourseActionListener {

    private lateinit var db: DBHelper
    private lateinit var rv: RecyclerView
    private lateinit var adapter: InstructorCourseAdapter
    private var instructorId: Int = -1
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_instructor_course_list)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = DBHelper(this)
        rv = findViewById(R.id.rvInstructorCourses)
        rv.layoutManager = LinearLayoutManager(this)

        username = intent.getStringExtra("username") ?: ""
        instructorId = db.getUserId(username) ?: -1

        loadCourses()

        findViewById<android.widget.Button>(R.id.btnInstructorBackToDash).setOnClickListener {
            finish()
        }
    }

    private fun loadCourses() {
        val list = if (instructorId >= 0) db.getCoursesByInstructor(instructorId) else listOf()
        adapter = InstructorCourseAdapter(list.toMutableList(), this)
        rv.adapter = adapter
    }

    // ----- callbacks from adapter -----
    override fun onEdit(course: Course) {
        // Show a simple dialog with two fields (title, description, price)
        val inflate = LayoutInflater.from(this)
        val v = inflate.inflate(android.R.layout.simple_list_item_2, null) // we'll build custom below

        val dialogView = layoutInflater.inflate(R.layout.item_instructor_course_edit_dialog, null)
        // The layout item_instructor_course_edit_dialog is not provided by default; create inline:
        // For brevity: use programmatic prompt
        val titleInput = EditText(this)
        titleInput.hint = "Title"
        titleInput.setText(course.title)
        val descInput = EditText(this)
        descInput.hint = "Description"
        descInput.setText(course.description)
        val priceInput = EditText(this)
        priceInput.hint = "Price"
        priceInput.setText(course.price.toString())

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        val pad = (8 * resources.displayMetrics.density).toInt()
        layout.setPadding(pad, pad, pad, pad)
        layout.addView(titleInput)
        layout.addView(descInput)
        layout.addView(priceInput)

        AlertDialog.Builder(this)
            .setTitle("Edit Course")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = titleInput.text.toString().trim()
                val newDesc = descInput.text.toString().trim()
                val newPrice = priceInput.text.toString().toDoubleOrNull() ?: course.price
                val ok = db.updateCourse(course.id, newTitle, newDesc, newPrice)
                if (ok) {
                    Toast.makeText(this, "Course updated", Toast.LENGTH_SHORT).show()
                    loadCourses()
                } else {
                    Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onDelete(course: Course) {
        AlertDialog.Builder(this)
            .setTitle("Delete course")
            .setMessage("Delete \"${course.title}\"? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val ok = db.deleteCourse(course.id)
                if (ok) {
                    Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show()
                    loadCourses()
                } else {
                    Toast.makeText(this, "Delete failed", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
