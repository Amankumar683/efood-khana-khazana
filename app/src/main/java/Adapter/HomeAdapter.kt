package Adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.khanakhajana.R
import com.example.khanakhajana.activity2.DescriptionActivity
import com.squareup.picasso.Picasso
import database.RestaurantDatabase
import database.RestaurantEntity
import model.Restaurant


class HomeAdapter(val context: Context,var itemArray:ArrayList<Restaurant>):RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val restaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val restaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val restaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val restaurantHeart: ImageView = view.findViewById(R.id.imgHeart)
        val homeLayout: LinearLayout = view.findViewById(R.id.homeLayout)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemArray[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantPrice.text = restaurant.restaurantPrice
        holder.restaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.ic_launcher_foreground).into(holder.restaurantImage)
        holder.homeLayout.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java).putExtra("id", restaurant.restaurantId).putExtra("name", restaurant.restaurantName).putExtra("rating", restaurant.restaurantRating).putExtra("cost_for_one", restaurant.restaurantPrice).putExtra("image", restaurant.restaurantImage)
            context.startActivity(intent)
        }
        val restaurantEntity = RestaurantEntity(
                restaurantId = restaurant.restaurantId,
                restaurantName = restaurant.restaurantName,
                restaurantPrice = restaurant.restaurantPrice,
                restaurantRating = restaurant.restaurantRating,
                restaurantImage = restaurant.restaurantImage
        )
        val checkFav = DBAsynkTask(context, restaurantEntity, 1).execute()
        val fav = checkFav.get()
        if (fav) {
            holder.restaurantHeart.setImageResource(R.drawable.ic_favourate)
        } else {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
        }
        holder.restaurantHeart.setOnClickListener {
            if (!DBAsynkTask(context, restaurantEntity, 1).execute().get()) {
                val async = DBAsynkTask(context, restaurantEntity, 2).execute()
                val value = async.get()
                if (value) {
                    Toast.makeText(context, "${restaurant.restaurantName} added to favourates", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_favourate)

                } else {
                    Toast.makeText(context, "some error occured", Toast.LENGTH_SHORT).show()

                }
            } else {
                val async = DBAsynkTask(context, restaurantEntity, 3).execute()
                val value = async.get()
                if (value) {
                    Toast.makeText(context, "${restaurant.restaurantName} removed from favourates", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)

                } else {
                    Toast.makeText(context, "some error occured", Toast.LENGTH_SHORT).show()

                }

            }
        }


    }

    override fun getItemCount(): Int {
        return itemArray.size
    }


    class DBAsynkTask(val context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) : AsyncTask<Void, Void, Boolean>() {
        val db = Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()
        override fun doInBackground(vararg params: Void?): Boolean {
            when (mode) {
                1 -> {

                    //check restaurant is favourate or not
                    val restaurantEntity: RestaurantEntity? =
                            db.restaurantdao().getRestaurantById(restaurantEntity.restaurantId)
                    db.close()
                    return restaurantEntity != null
                }
                2 -> {
                    //save the book in db

                    db.restaurantdao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
                3 -> {
                    //remove the book from db

                    db.restaurantdao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }
            }
            return false

        }

    }
}