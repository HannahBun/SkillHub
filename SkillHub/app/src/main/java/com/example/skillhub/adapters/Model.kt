package com.example.skillhub.adapters


data class Course(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val instructorId: Int
)

data class Instructor(
    val id: Int,
    val name: String,
    val email: String
)
