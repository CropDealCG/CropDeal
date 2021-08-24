package com.cg.cropdeal.model

import androidx.room.*


@Entity(tableName = "userID", indices = arrayOf(Index(value = ["id"],
    unique = true)))
data class UsersIDRepo
    (
    @ColumnInfo(name = "id") val userID : String,
    @ColumnInfo (name = "user") val user: Users

    )