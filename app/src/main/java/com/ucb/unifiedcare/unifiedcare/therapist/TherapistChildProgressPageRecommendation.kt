package com.ucb.unifiedcare.unifiedcare.therapist

import Adapter.ObservationAdapter
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ucb.unifiedcare.R
import com.ucb.unifiedcare.databinding.ActivityTherapistChildProgressPageRecommendationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TherapistChildProgressPageRecommendation : AppCompatActivity() {
    private lateinit var binding: ActivityTherapistChildProgressPageRecommendationBinding
    private var currentWeekOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTherapistChildProgressPageRecommendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Display the current week on launch
        displayWeek()

        // Set listeners for week navigation
        binding.leftArrow.setOnClickListener {
            currentWeekOffset -= 1
            displayWeek()
        }

        binding.rightArrow.setOnClickListener {
            currentWeekOffset += 1
            displayWeek()
        }

        // Placeholder data to simulate a database content
        val recommendationTitles = listOf("Add Session")
        // Set up CustomAdapter with placeholder data
        val observationAdapter = ObservationAdapter(this, recommendationTitles)
        binding.listview.adapter = observationAdapter

    }

    private fun displayWeek() {
        // Clear previous views from the containers
        binding.daysOfWeekContainer.removeAllViews()
        binding.datesContainer.removeAllViews()

        // Set up date formatters
        val dayFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())

        // Get calendar instance and set to current week
        val calendar = Calendar.getInstance()
        calendar.firstDayOfWeek = Calendar.MONDAY

        // Adjust for week offset
        calendar.add(Calendar.WEEK_OF_YEAR, currentWeekOffset)

        // Move to start of the week (Monday)
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DATE, -1)
        }

        // Loop through the week (Monday to Sunday)
        for (i in 0..6) {
            val dayText = dayFormat.format(calendar.time)
            val dateText = dateFormat.format(calendar.time)

            // Create a TextView for the day
            val dayView = TextView(this).apply {
                text = dayText
                textSize = 16f
                setPadding(16, 8, 16, 8)
            }

            // Create a TextView for the date
            val dateView = TextView(this).apply {
                text = dateText
                textSize = 16f
                setPadding(12, 8, 12, 8)

                // Optional: Highlight current day with a background color
                if (calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance()
                        .get(Calendar.DAY_OF_MONTH)
                ) {
                    setBackgroundResource(R.drawable.day_background_selector)
                }
            }

            // Add day and date TextViews to their respective containers
            binding.daysOfWeekContainer.addView(dayView)
            binding.datesContainer.addView(dateView)

            // Move to the next day
            calendar.add(Calendar.DATE, 1)
        }

        // Update month/year title
        val monthYearFormat = SimpleDateFormat("MMMM yyyy", Locale.getDefault())
        binding.month.text = monthYearFormat.format(calendar.time)
    }

}