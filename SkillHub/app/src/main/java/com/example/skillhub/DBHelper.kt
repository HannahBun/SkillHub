package com.example.skillhub

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.skillhub.adapters.Course

class DBHelper(context: Context) : SQLiteOpenHelper(context, "skillhub.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users (id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT UNIQUE, password TEXT, role TEXT)"
        )

        db.execSQL(
            "CREATE TABLE courses (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, description TEXT, price REAL, instructor_id INTEGER)"
        )

        db.execSQL(
            "CREATE TABLE enrollments (id INTEGER PRIMARY KEY AUTOINCREMENT, user_id INTEGER, course_id INTEGER)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS enrollments")
        db.execSQL("DROP TABLE IF EXISTS courses")
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun registerUser(username: String, password: String, role: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("username", username)
            put("password", password)
            put("role", role)
        }
        return try {
            db.insert("users", null, values) != -1L
        } catch (_: Exception) {
            false
        }
    }

    fun loginUser(username: String, password: String): String? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT role FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        val role = if (cursor.moveToFirst()) cursor.getString(0) else null
        cursor.close()
        return role
    }

    fun getUserId(username: String): Int? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT id FROM users WHERE username=?", arrayOf(username))
        val id = if (cursor.moveToFirst()) cursor.getInt(0) else null
        cursor.close()
        return id
    }

    fun addCourse(title: String, description: String, price: Double, instructorId: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("price", price)
            put("instructor_id", instructorId)
        }
        return db.insert("courses", null, values)
    }

    fun updateCourse(courseId: Int, title: String, description: String, price: Double): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("description", description)
            put("price", price)
        }
        val updated = db.update("courses", values, "id=?", arrayOf(courseId.toString()))
        return updated > 0
    }

    fun deleteCourse(courseId: Int): Boolean {
        val db = writableDatabase
        val deleted = db.delete("courses", "id=?", arrayOf(courseId.toString()))
        return deleted > 0
    }

    fun getAllCourses(): List<Course> {
        val db = readableDatabase
        val list = mutableListOf<Course>()
        val cursor = db.rawQuery("SELECT id, title, description, price, instructor_id FROM courses", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Course(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getCoursesByInstructor(instructorId: Int): List<Course> {
        val db = readableDatabase
        val list = mutableListOf<Course>()
        val cursor = db.rawQuery(
            "SELECT id, title, description, price, instructor_id FROM courses WHERE instructor_id=?",
            arrayOf(instructorId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Course(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }

    fun getCourseById(courseId: Int): Course? {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT id, title, description, price, instructor_id FROM courses WHERE id=?",
            arrayOf(courseId.toString())
        )
        val course = if (cursor.moveToFirst()) {
            Course(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getDouble(3),
                cursor.getInt(4)
            )
        } else null
        cursor.close()
        return course
    }

    fun enrollInCourse(userId: Int, courseId: Int): Boolean {
        val db = readableDatabase
        val check = db.rawQuery("SELECT id FROM enrollments WHERE user_id=? AND course_id=?", arrayOf(userId.toString(), courseId.toString()))
        if (check.moveToFirst()) {
            check.close()
            return false
        }
        check.close()

        val wdb = writableDatabase
        val values = ContentValues().apply {
            put("user_id", userId)
            put("course_id", courseId)
        }
        return wdb.insert("enrollments", null, values) != -1L
    }

    fun getEnrolledCourses(userId: Int): List<Course> {
        val db = readableDatabase
        val list = mutableListOf<Course>()
        val cursor = db.rawQuery(
            "SELECT c.id, c.title, c.description, c.price, c.instructor_id FROM courses c " +
                    "INNER JOIN enrollments e ON c.id = e.course_id WHERE e.user_id=?",
            arrayOf(userId.toString())
        )
        if (cursor.moveToFirst()) {
            do {
                list.add(
                    Course(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getInt(4)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return list
    }
}
