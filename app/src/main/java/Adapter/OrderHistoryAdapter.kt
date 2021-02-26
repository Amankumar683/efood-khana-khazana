package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khanakhajana.R
import model.Order
import java.util.ArrayList

class OrderHistoryAdapter(val context: Context, var itemArray: ArrayList<Order>):RecyclerView.Adapter<OrderHistoryAdapter.OrderHistoryViewHolder>() {
    class OrderHistoryViewHolder(view:View):RecyclerView.ViewHolder(view)
    {
        val dishName:TextView=view.findViewById(R.id.txtDishName)
        val dishPrice:TextView=view.findViewById(R.id.txtDishPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHistoryViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_cart,parent,false)
        return OrderHistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val dish = itemArray[position]
        holder.dishName.text = dish.dishName
        val temp = "₹"+ dish.dishPrice
        holder.dishPrice.text = temp
    }

    override fun getItemCount(): Int {
        return itemArray.size
    }
}