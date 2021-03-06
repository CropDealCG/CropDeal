package com.cg.cropdeal.model.dao

import androidx.room.*
import com.cg.cropdeal.model.Crops

@Dao
interface CropDAO {
    @Insert
    suspend fun insert(crop : Crops)
    @Update
    suspend fun update(crop: Crops)
    @Delete
    suspend fun delete(crop: Crops)
    /*@Query("select * from cropsDb")
    suspend fun getCrops():List<Crops>*/
    @Query("select * from cropsDb where cropId=:cropId")
    suspend fun getCropByID(cropId:String):List<Crops>
}