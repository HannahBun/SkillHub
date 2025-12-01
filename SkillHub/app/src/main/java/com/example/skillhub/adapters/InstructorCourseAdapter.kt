package com.example.skillhub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.R

class InstructorCourseAdapter(
    private val items: MutableList<Course>,
    private val onEdit: (Course) -> Unit,
    private val onDelete: (Course) -> Unit
) : RecyclerView.Adapter<InstructorCourseAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvInstructorItemTitle)
        val desc: TextView = view.findViewById(R.id.tvInstructorItemDesc)
        val btnEdit: Button = view.findViewById(R.id.btnEditCourse)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_instructor_course, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val course = items[position]
        holder.title.text = course.title
        holder.desc.text = course.description

        holder.btnEdit.setOnClickListener {
            onEdit(course)
        }

        holder.btnDelete.setOnClickListener {
            onDelete(course)
        }
    }

    override fun getItemCount(): Int = items.size


    fun updateList(newList: List<Course>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
