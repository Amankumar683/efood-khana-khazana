package database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RestaurantDao {
    @Insert
    fun insertRestaurant(restaurantEntity : RestaurantEntity)

    @Delete
    fun deleteRestaurant(restaurantEntity : RestaurantEntity)
    @Query("Select * FROM Restaurants")
    fun getAllRestaurants(): List<RestaurantEntity>
    @Query("Select * FROM Restaurants WHERE restaurant_Id =:restaurantId")
    fun getRestaurantById(restaurantId: String): RestaurantEntity
}