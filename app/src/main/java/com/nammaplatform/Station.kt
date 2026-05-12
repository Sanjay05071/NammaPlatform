package com.nammaplatform

data class Coach(
    val type: String,        // ENGINE, GENERAL, LADIES, SLEEPER, AC, GUARD
    val label: String,       // Short label e.g. "GEN", "S1"
    val labelKn: String      // Kannada label
)

data class Train(
    val number: String,
    val nameKannada: String,
    val nameEnglish: String,
    val destinationKannada: String,
    val destinationEnglish: String,
    val arrivalTime: String,
    val platform: Int,
    val status: String,       // "on_time" | "delayed"
    val statusKannada: String,
    val coaches: List<Coach>,
    val announcementKannada: String
)

data class Station(
    val id: String,
    val nameKannada: String,
    val nameEnglish: String,
    val code: String,
    val trains: List<Train>
)
