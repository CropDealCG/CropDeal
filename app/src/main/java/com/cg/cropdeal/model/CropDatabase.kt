package com.cg.cropdeal.model

import android.content.Context
import androidx.room.*

@Database(entities = [Crops::class],version = 1)

abstract class CropDatabase: RoomDatabase() {
    abstract  fun cropDao(): CropDAO

    companion object{
        private var instance: CropDatabase? = null
        fun getInstance(context: Context)= instance?:buildDatabase(context).also{ instance= it}
        private fun buildDatabase(context: Context): CropDatabase{
            val builder= Room.databaseBuilder(context,CropDatabase::class.java,"crops.db")
            return builder.build()
        }
    }
}