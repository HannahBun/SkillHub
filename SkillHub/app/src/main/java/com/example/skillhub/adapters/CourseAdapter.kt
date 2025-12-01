package com.example.skillhub.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.skillhub.DBHelper
import com.example.skillhub.Payment
import com.example.skillhub.R
import com.example.skillhub.Search

class CourseAdapter(
    private val context: Search,
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


        val enrolledCourses = db.getEnrolledCourses(userId)
        if (enrolledCourses.any { it.id == course.id }) {
            btnEnroll.isEnabled = false
            btnEnroll.text = "Enrolled"
        } else {
            btnEnroll.isEnabled = true
            btnEnroll.text = "Enroll"
        }

        btnEnroll.setOnClickListener {
            val enrolled = db.getEnrolledCourses(userId)
            if (enrolled.any { it.id == course.id }) {
                Toast.makeText(context, "Already enrolled", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, Payment::class.java)
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
