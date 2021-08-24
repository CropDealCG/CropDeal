package com.cg.cropdeal.model

import androidx.room.*

@Dao
interface UserIdDAO {
    @Insert
    suspend fun insert(users : UsersIDRepo)
    @Update
    suspend fun update(users : UsersIDRepo)
    @Delete
    suspend fun delete(users : UsersIDRepo)
    @Query("select * from userId")
    suspend fun getUsers():List<UsersIDRepo>
    @Query("select * from userId where id=:id")
    suspend fun getUsersByID(id:String):List<UsersIDRepo>

}