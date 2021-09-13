package com.cg.cropdeal.model.dao

import androidx.room.*
import com.cg.cropdeal.model.UsersID

@Dao
interface UserIdDAO {
    @Insert
    suspend fun insert(users : UsersID)
    @Update
    suspend fun update(users : UsersID)
    @Delete
    suspend fun delete(users : UsersID)
    /*@Query("select * from userId")
    suspend fun getUsers():List<UsersIDRepo>
    @Query("select * from userId where id=:id")
    suspend fun getUsersByID(id:String):List<UsersIDRepo>*/

}