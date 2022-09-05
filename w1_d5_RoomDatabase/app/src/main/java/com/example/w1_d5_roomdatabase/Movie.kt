package com.example.w1_d5_roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Movie(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val year: Int,
    val director: String,
) {
    override fun toString(): String {
        return "Movie(idMovie=$id, title='$title', year=$year, director='$director')"
    }
}
