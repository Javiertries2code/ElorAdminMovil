package com.elorrieta.alumnoclient.room



import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "users")
data class RoomUser(
    @PrimaryKey(autoGenerate = true) val id :Long,
    var email: String,

    var password: String,

    var lastLogin: Long,

    var remember: Boolean
) {
}