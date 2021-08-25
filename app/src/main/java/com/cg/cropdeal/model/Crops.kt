package com.cg.cropdeal.model

import androidx.room.*

@Entity(tableName = "cropsDb")
data class Crops(
    @PrimaryKey
    var cropId : String,
    var cropName : String,
    var cropType : String,
    var cropQuantity : Int,
    var cropPrice : Int,
    var cropLocation : String,
    var cropDesc : String,
    var userId : String
){
    constructor() : this("","","",0,0,"","","")
}