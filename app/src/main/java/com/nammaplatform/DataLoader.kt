package com.nammaplatform

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object DataLoader {

    fun loadStations(context: Context): List<Station> {
        val json = context.assets.open("stations.json")
            .bufferedReader().use { it.readText() }

        val root = JSONObject(json)
        val stationsArray = root.getJSONArray("stations")
        val stations = mutableListOf<Station>()

        for (i in 0 until stationsArray.length()) {
            val stationObj = stationsArray.getJSONObject(i)
            val trains = parseTrains(stationObj.getJSONArray("trains"))
            stations.add(
                Station(
                    id = stationObj.getString("id"),
                    nameKannada = stationObj.getString("name_kannada"),
                    nameEnglish = stationObj.getString("name_english"),
                    code = stationObj.getString("code"),
                    trains = trains
                )
            )
        }
        return stations
    }

    private fun parseTrains(trainsArray: JSONArray): List<Train> {
        val trains = mutableListOf<Train>()
        for (i in 0 until trainsArray.length()) {
            val t = trainsArray.getJSONObject(i)
            trains.add(
                Train(
                    number = t.getString("number"),
                    nameKannada = t.getString("name_kannada"),
                    nameEnglish = t.getString("name_english"),
                    destinationKannada = t.getString("destination_kannada"),
                    destinationEnglish = t.getString("destination_english"),
                    arrivalTime = t.getString("arrival_time"),
                    platform = t.getInt("platform"),
                    status = t.getString("status"),
                    statusKannada = t.getString("status_kannada"),
                    coaches = parseCoaches(t.getJSONArray("coaches")),
                    announcementKannada = t.getString("announcement_kannada")
                )
            )
        }
        return trains
    }

    private fun parseCoaches(coachesArray: JSONArray): List<Coach> {
        val coaches = mutableListOf<Coach>()
        for (i in 0 until coachesArray.length()) {
            val c = coachesArray.getJSONObject(i)
            coaches.add(
                Coach(
                    type = c.getString("type"),
                    label = c.getString("label"),
                    labelKn = c.getString("label_kn")
                )
            )
        }
        return coaches
    }
}
