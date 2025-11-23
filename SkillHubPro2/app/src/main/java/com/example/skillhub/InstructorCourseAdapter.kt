package com.example.skillhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class InstructorCourseAdapter(
    private val items: MutableList<Course>,
    private val listener: OnCourseActionListener
) : RecyclerView.Adapter<InstructorCourseAdapter.VH>() {

    interface OnCourseActionListener {
        fun onEdit(course: Course)
        fun onDelete(course: Course)
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvInstructorItemTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvInstructorItemDesc)
        val btnEdit: Button = view.findViewById(R.id.btnEditCourse)
        val btnDelete: Button = view.findViewById(R.id.btnDeleteCourse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_instructor_course, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.tvTitle.text = c.title
        holder.tvDesc.text = c.description
        holder.btnEdit.setOnClickListener { listener.onEdit(c) }
        holder.btnDelete.setOnClickListener { listener.onDelete(c) }
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newList: List<Course>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
