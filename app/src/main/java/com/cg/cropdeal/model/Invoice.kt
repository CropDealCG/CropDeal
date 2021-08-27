package com.cg.cropdeal.model

data class Invoice
    (
        val cropId : String,
        val farmerId : String,
        val buyerId : String,
        val date : String,
        val time : String,
        val quantity : Int,
        val rate : Int,
        val totalPrice : Int,
        val cropName : String
    ){
        constructor():this("","","","","",0,0,0,"")
    }