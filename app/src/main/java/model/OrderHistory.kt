package model

import java.util.ArrayList

data class OrderHistory(val restaurantName:String, val orderDate:String, val foodArray: ArrayList<Order>)
