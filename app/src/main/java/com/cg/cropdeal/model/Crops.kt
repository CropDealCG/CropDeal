package com.cg.cropdeal.model

data class Crops(
    val cropName : String,
    val cropType : String,
    val cropQuantity : Int,
    val cropPrice : Int,
    val cropLocation : String,
    val cropDesc : String,
    val userId : String
){
    constructor() : this("","",0,0,"","","")
}