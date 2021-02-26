package database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import model.Restaurant

@Entity(tableName = "Restaurants")
data class RestaurantEntity (@PrimaryKey @ColumnInfo(name = "restaurant_Id") val restaurantId:String,
@ColumnInfo(name = "restaurant_Name") val restaurantName:String,
@ColumnInfo(name = "restaurant_Rating") val restaurantRating:String,
@ColumnInfo(name = "restaurant_Price") val restaurantPrice:String,
@ColumnInfo(name = "restaurant_Image") val restaurantImage:String)
