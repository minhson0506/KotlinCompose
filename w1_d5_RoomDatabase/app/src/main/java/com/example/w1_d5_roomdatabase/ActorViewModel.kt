package com.example.w1_d5_roomdatabase

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ActorViewModel(application: Application): AndroidViewModel(application) {
    private val movieDB = MovieDB.get(application)

    fun getById(id: Int): LiveData<List<Actor>> = movieDB.actorDao().getActorWithIdMovie(id = id)

    fun insert(actor: Actor) {
        viewModelScope.launch {
            movieDB.actorDao().insert(actor = actor)
        }
    }
}