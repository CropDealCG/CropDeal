package com.cg.cropdeal.model

import androidx.room.*
import com.airbnb.lottie.L

@Dao
interface UserIdDAO {
    @Insert
    suspend fun insert(userID: String, user : Users)
    @Update
    suspend fun update(userID: String, user : Users)
    @Delete
    suspend fun delete(userID: String, user : Users)
    @Query("select * from userID")
    suspend fun getUsers():List<UsersIDRepo>

    @Query("select * from userID where id=:id")
    suspend fun getUsersByID(id:String):List<UsersIDRepo>

}