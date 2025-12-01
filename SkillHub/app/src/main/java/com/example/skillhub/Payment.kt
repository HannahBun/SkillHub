package com.example.skillhub

import android.app.Activity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class Payment : AppCompatActivity() {

    private lateinit var db: DBHelper
    private var courseId: Int = -1
    private var userId: Int = -1
    private lateinit var tvCourseTitle: TextView
    private lateinit var tvCoursePrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        db = DBHelper(this)

        tvCourseTitle = findViewById(R.id.tvPaymentCourseTitle)
        tvCoursePrice = findViewById(R.id.tvPaymentCoursePrice)

        val etName = findViewById<EditText>(R.id.etCardName)
        val etNumber = findViewById<EditText>(R.id.etCardNumber)
        val etMonth = findViewById<EditText>(R.id.etExpMonth)
        val etYear = findViewById<EditText>(R.id.etExpYear)
        val etCvv = findViewById<EditText>(R.id.etCvv)
        val btnPay = findViewById<Button>(R.id.btnPayNow)


        val username = intent.getStringExtra("username") ?: ""
        courseId = intent.getIntExtra("courseId", -1)
        userId = db.getUserId(username) ?: -1

        val course = db.getCourseById(courseId)
        if (course != null) {
            tvCourseTitle.text = course.title
            tvCoursePrice.text = "$${"%.2f".format(course.price)}"
        } else {
            tvCourseTitle.text = "Unknown course"
            tvCoursePrice.text = "$0.00"
        }

        btnPay.setOnClickListener {
            val name = etName.text.toString().trim()
            val number = etNumber.text.toString().trim()
            val month = etMonth.text.toString().trim()
            val year = etYear.text.toString().trim()
            val cvv = etCvv.text.toString().trim()


            when {
                !name.matches(Regex("^[A-Za-z ]+$")) -> {
                    Toast.makeText(this, "Enter valid name", Toast.LENGTH_SHORT).show()
                }
                !number.matches(Regex("^\\d{16}$")) -> {
                    Toast.makeText(this, "Card number must be 16 digits", Toast.LENGTH_SHORT).show()
                }
                !month.matches(Regex("^\\d{1,2}$")) || month.toInt() !in 1..12 -> {
                    Toast.makeText(this, "Enter valid month", Toast.LENGTH_SHORT).show()
                }
                !year.matches(Regex("^\\d{4}$")) -> {
                    Toast.makeText(this, "Enter valid 4-digit year", Toast.LENGTH_SHORT).show()
                }
                !cvv.matches(Regex("^\\d{3}$")) -> {
                    Toast.makeText(this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show()
                }
                userId < 0 || courseId < 0 -> {
                    Toast.makeText(this, "User or course not recognized", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    val success = db.enrollInCourse(userId, courseId)
                    if (success) {
                        Toast.makeText(this, "Payment processed — enrolled!", Toast.LENGTH_LONG).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Already enrolled or error occurred", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
