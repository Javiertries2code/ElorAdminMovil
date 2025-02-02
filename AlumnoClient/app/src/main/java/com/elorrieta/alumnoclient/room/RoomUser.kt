package com.elorrieta.alumnoclient.room



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class RoomUser(
    @PrimaryKey(autoGenerate = true) val id :Long ,
    val email: String,

    val password: String,

    val lastLogin: Long,

    val remember: Boolean
) {
}