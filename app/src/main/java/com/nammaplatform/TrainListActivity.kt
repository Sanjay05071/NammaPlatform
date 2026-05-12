package com.nammaplatform

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TrainListActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_STATION_ID = "STATION_ID"
    }

    lateinit var stationId: String
    private lateinit var stations: List<Station>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train_list)

        stationId = intent.getStringExtra(EXTRA_STATION_ID) ?: ""
        stations = DataLoader.loadStations(this)

        val station = stations.find { it.id == stationId }
            ?: run { finish(); return }

        // Bind header
        findViewById<TextView>(R.id.tvStationName).text = station.nameKannada
        findViewById<TextView>(R.id.tvStationCode).text = "${station.nameEnglish} | ${station.code}"

        // Setup RecyclerView
        val rvTrains = findViewById<RecyclerView>(R.id.rvTrains)
        rvTrains.layoutManager = LinearLayoutManager(this)
        rvTrains.adapter = TrainAdapter(this, station.trains)

        // Back button
        findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            finish()
        }
    }
}
