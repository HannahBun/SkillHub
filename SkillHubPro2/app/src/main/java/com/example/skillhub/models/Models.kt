package com.example.skillhub

data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val instructorId: Int // new field
)
