package com.cg.cropdeal.model

import androidx.room.*

@Dao
interface CropDAO {
    @Insert
    suspend fun insert(crop : CropRepo)
    @Update
    suspend fun update(crop: CropRepo)
    @Delete
    suspend fun delete(crop: CropRepo)
    @Query("select * from crops")
    suspend fun getCrops():List<CropRepo>
    @Query("select * from crops where cropId=:cropId")
    suspend fun getCropByID(cropId:String):List<CropRepo>
}