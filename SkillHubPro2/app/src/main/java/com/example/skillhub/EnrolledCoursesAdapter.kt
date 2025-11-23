package com.example.skillhub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EnrolledCourseAdapter(private val items: List<Course>) : RecyclerView.Adapter<EnrolledCourseAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvEnrolledItemTitle)
        val tvDesc: TextView = view.findViewById(R.id.tvEnrolledItemDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_enrolled_course, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.tvTitle.text = c.title
        holder.tvDesc.text = c.description
    }

    override fun getItemCount(): Int = items.size
}
