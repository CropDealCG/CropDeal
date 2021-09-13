package com.cg.cropdeal.model.database

import android.content.Context
import androidx.room.*
import com.cg.cropdeal.model.dao.UserIdDAO
import com.cg.cropdeal.model.UsersID


@Database(entities = [UsersID::class], version = 1, exportSchema = false)

abstract class UserIdDatabase: RoomDatabase() {
    abstract fun userIdDao(): UserIdDAO

    companion object{
        private var instance: UserIdDatabase? = null
        fun getInstance(context:Context)= instance ?: buildDatabase(context).also{ instance = it}
        private fun buildDatabase(context: Context): UserIdDatabase {
            val builder= Room.databaseBuilder(context, UserIdDatabase::class.java,"userid.db")
            return builder.build()
        }
    }
}