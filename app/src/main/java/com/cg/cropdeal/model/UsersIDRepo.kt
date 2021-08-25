package com.cg.cropdeal.model

import androidx.room.*

@Entity(tableName = "userId")
data class UsersIDRepo
    (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var userID : String,
    @ColumnInfo(name = "userEmail")
    var userEmail : String,
    )