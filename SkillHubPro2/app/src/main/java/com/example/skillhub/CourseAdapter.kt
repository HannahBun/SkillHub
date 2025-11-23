package com.example.skillhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.models.Course

class CourseAdapter(
    private var courses: List<Course>
) : RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    private val fullList = courses.toList()

    inner class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCourseTitle: TextView = view.findViewById(R.id.tvCourseTitle)
        val tvInstructorName: TextView = view.findViewById(R.id.tvInstructorName)
        val tvTiming: TextView = view.findViewById(R.id.tvTiming)
        val tvPrice: TextView = view.findViewById(R.id.tvPrice)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.course_list_item, parent, false)
        return CourseViewHolder(view)
    }

    override fun getItemCount(): Int = courses.size

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.tvCourseTitle.text = course.title
        holder.tvInstructorName.text = "Instructor: ${course.instructorName}"
        holder.tvTiming.text = "Timing: ${course.timing}"
        holder.tvPrice.text = "Price: $${course.price}"
        holder.tvRating.text = "Rating: ${course.rating}"
    }

    fun filter(query: String) {
        val q = query.lowercase()
        courses = if (q.isEmpty()) {
            fullList
        } else {
            fullList.filter {
                it.title.lowercase().contains(q) ||
                it.instructorName.lowercase().contains(q)
            }
        }
        notifyDataSetChanged()
    }
}

