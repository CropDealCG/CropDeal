package com.cg.cropdeal.view

import java.sql.Time
import java.util.*

data class Users (val name: String,val email: String,val type: String,
                  val isAdmin: String, val date: String,val time: String)
{
    constructor():this("","","","","","")

}