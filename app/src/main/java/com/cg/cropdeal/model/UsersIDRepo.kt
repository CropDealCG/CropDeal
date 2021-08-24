package com.cg.cropdeal.model

import androidx.room.*

@Entity(tableName = "userId")
data class UsersIDRepo
    (
    @PrimaryKey
    @ColumnInfo(name = "id")
    val userID : String,
    @ColumnInfo (name = "userName")
    val userName: String,
    @ColumnInfo(name = "userEmail")
    val userEmail : String,
    @ColumnInfo(name = "userType")
    val userType : String,
    )