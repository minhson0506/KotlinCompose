package com.example.w1_d5_roomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieDB = MovieDB.get(application)

    fun getALl(): LiveData<List<Movie>> = movieDB.movieDao().getAll()

    fun insert(movie: Movie) {
        viewModelScope.launch { movieDB.movieDao().insert(movie) }
    }
}