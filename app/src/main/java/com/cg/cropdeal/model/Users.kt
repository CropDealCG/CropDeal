package com.cg.cropdeal.model

import java.sql.Time
import java.util.*

data class Users (val name: String,val email: String,val type: String,
                  val admin: String, val date: String,val time: String
                  ,val payment: Payment,val active: Boolean, val rating: Double,
                   val noOfRating: Int,val noOfCars: Int)
{
    constructor():this("","","","","","",Payment(),true,0.0,0,1)

}