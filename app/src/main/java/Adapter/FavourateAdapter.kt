package Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.khanakhajana.R
import com.example.khanakhajana.activity2.DescriptionActivity
import com.squareup.picasso.Picasso
import database.RestaurantEntity

class FavourateAdapter(val context: Context,val itemArray:List<RestaurantEntity>) :RecyclerView.Adapter<FavourateAdapter.FavourateViewHolder>() {
    class FavourateViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val restaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val restaurantPrice: TextView = view.findViewById(R.id.txtRestaurantPrice)
        val restaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val restaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val restaurantHeart: ImageView = view.findViewById(R.id.imgHeart)
        val homeLayout: LinearLayout = view.findViewById(R.id.homeLayout)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavourateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourate, parent, false)
        return FavourateViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavourateViewHolder, position: Int) {
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
        val checkFav = HomeAdapter.DBAsynkTask(context, restaurantEntity, 1).execute()
        val fav = checkFav.get()
        if (fav) {
            holder.restaurantHeart.setImageResource(R.drawable.ic_favourate)
        } else {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
        }
        holder.restaurantHeart.setOnClickListener {
            if (!HomeAdapter.DBAsynkTask(context, restaurantEntity, 1).execute().get()) {
                val async = HomeAdapter.DBAsynkTask(context, restaurantEntity, 2).execute()
                val value = async.get()
                if (value) {
                    Toast.makeText(context, "${restaurant.restaurantName} added to favourates", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_favourate)


                } else {
                    Toast.makeText(context, "some error occured", Toast.LENGTH_SHORT).show()

                }
            } else {
                val async = HomeAdapter.DBAsynkTask(context, restaurantEntity, 3).execute()
                val value = async.get()
                if (value) {
                    Toast.makeText(context, "${restaurant.restaurantName} removed from favourates", Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
                    //notifyDataSetChanged()

                } else {
                    Toast.makeText(context, "some error occured", Toast.LENGTH_SHORT).show()

                }

            }
        }

    }

    override fun getItemCount(): Int {
        return itemArray.size
    }
}
