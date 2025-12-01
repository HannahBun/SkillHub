package com.example.skillhub.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.skillhub.R

class EnrolledCourseAdapter(
    private val items: List<Course>
) : RecyclerView.Adapter<EnrolledCourseAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.tvEnrolledItemTitle)
        val desc: TextView = view.findViewById(R.id.tvEnrolledItemDesc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_enrolled_course, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = items[position]
        holder.title.text = c.title
        holder.desc.text = c.description
    }

    override fun getItemCount(): Int = items.size
}
