package com.cg.cropdeal.model

import androidx.room.*

@Entity(tableName = "crops")
data class Crops(
    @PrimaryKey
    val cropId : String,
    val cropName : String,
    val cropType : String,
    val cropQuantity : Int,
    val cropPrice : Int,
    val cropLocation : String,
    val cropDesc : String,
    val userId : String
){
    constructor() : this("","","",0,0,"","","")
}