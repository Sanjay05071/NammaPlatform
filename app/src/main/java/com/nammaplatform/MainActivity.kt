package com.nammaplatform

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var stations: List<Station>
    private lateinit var spinnerStation: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stations = DataLoader.loadStations(this)

        spinnerStation = findViewById(R.id.spinnerStation)

        // Spinner shows: "ಅರಸೀಕೆರೆ (Arsikere)"
        val displayNames = stations.map { "${it.nameKannada} (${it.nameEnglish})" }
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            displayNames
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerStation.adapter = adapter

        val btnShowTrains = findViewById<Button>(R.id.btnShowTrains)
        btnShowTrains.setOnClickListener {
            val selectedIndex = spinnerStation.selectedItemPosition
            if (selectedIndex < 0) {
                Toast.makeText(this, "ನಿಲ್ದಾಣ ಆಯ್ಕೆ ಮಾಡಿ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val station = stations[selectedIndex]
            val intent = Intent(this, TrainListActivity::class.java).apply {
                putExtra(TrainListActivity.EXTRA_STATION_ID, station.id)
            }
            startActivity(intent)
        }
    }
}
