package com.cg.cropdeal.model

import android.content.Context
import androidx.room.*


@Database(entities = [UsersIDRepo::class], version = 1)

abstract class UserIdDatabase: RoomDatabase() {
    abstract fun userIdDao(): UserIdDAO

    companion object{
        private var instance: UserIdDatabase? = null
        fun getInstance(context:Context)= instance?:buildDatabase(context).also{ instance= it}
        private fun buildDatabase(context: Context): UserIdDatabase{
            val builder= Room.databaseBuilder(context,UserIdDatabase::class.java,"userid.db")
            return builder.build()
        }
    }
}