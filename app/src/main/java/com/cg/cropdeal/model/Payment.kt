package com.cg.cropdeal.model

data class Payment(
    val bank:String,
    val account: Long,
    val ifsc:String,
    val userId:String
)
{
    constructor(): this("",0,"","")
}