package com.elorrieta.alumnoclient.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface RoomDAO {

    @Query("SELECT * FROM users ORDER BY lastLogin DESC LIMIT 1")
    suspend fun getLastLoggedUser(): RoomUser?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(roomUser: RoomUser)

    @Delete
    suspend fun delete(roomUser: RoomUser): Int
}
