package com.cg.cropdeal.model

import java.sql.Time
import java.util.*

data class Users (val name: String,val email: String,val type: String,
                  val isAdmin: String, val date: String,val time: String
                  ,val payment: Payment,val isActive: Boolean)
{
    constructor():this("","","","","","",Payment(),true)

}