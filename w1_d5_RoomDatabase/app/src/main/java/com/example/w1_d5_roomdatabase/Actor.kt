package com.example.w1_d5_roomdatabase

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(foreignKeys = [ForeignKey(entity = Movie::class,
    onDelete = CASCADE,
    parentColumns = ["id"],
    childColumns = ["id"])])
data class Actor(
    val id: Int,
    @PrimaryKey
    val name: String,
    val role: String
) {
    override fun toString(): String {
        return "Actor(id movie=$id, name='$name', role='$role')"
    }
}

class ActorMovie {
    @Embedded
    var movie: Movie? = null
    @Relation(parentColumn = "id", entityColumn = "id")
    var actors: List<Actor>? = null
}
