package com.cg.cropdeal.model

import androidx.room.*

@Entity(tableName = "userId")
data class UsersIDRepo
    (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val userID : String,
    @ColumnInfo(name = "userEmail")
    val userEmail : String,
    )