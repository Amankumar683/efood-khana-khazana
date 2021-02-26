package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khanakhajana.R
import model.Dish
import java.util.ArrayList

class CartAdapter(val context: Context, val orderArrayList: ArrayList<Dish>?):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {
    class CartViewHolder(view: View):RecyclerView.ViewHolder(view)
    {

        val dishName:TextView=view.findViewById(R.id.txtDishName)
        val dishPrice:TextView=view.findViewById(R.id.txtDishPrice)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart,parent,false)
        return CartViewHolder(view)

    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        val orderCart= orderArrayList?.get(position)
        holder.dishName.text=orderCart?.dishName
        holder.dishPrice.text=orderCart?.dishPrice
    }

    override fun getItemCount(): Int {
        return orderArrayList?.size!!
    }
}