package com.example.w1_d5_roomdatabase

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {
    @Query("SELECT * FROM movie")
    fun getAll(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Update
    suspend fun update(movie: Movie)

    @Delete
    suspend fun delete(movie: Movie)
}

@Dao
interface ActorDao {
    @Query("SELECT * FROM actor")
    fun getAll(): LiveData<List<Actor>>

    @Query("SELECT * FROM actor where actor.id = :id")
    fun getActorWithIdMovie(id: Int): LiveData<List<Actor>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: Actor)

    @Update
    suspend fun update(actor: Actor)

    @Delete
    suspend fun delete(actor: Actor)
}