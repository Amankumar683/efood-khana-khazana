package Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.khanakhajana.R
import model.OrderHistory

class OrderAdapter( context: Context,var orderArray:ArrayList<OrderHistory>):RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    var context=context
    class OrderViewHolder(val view: View):RecyclerView.ViewHolder(view)
    {
        val restaurantName:TextView=view.findViewById(R.id.txtRestaurantName)
        val orderDate:TextView=view.findViewById(R.id.txtDate)
        val recyclerHistory:RecyclerView=view.findViewById(R.id.recyclerHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.recycler_order,parent,false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order=orderArray[position]
        holder.restaurantName.text=order.restaurantName
        holder.orderDate.text=order.orderDate
        var recyclerAdapter=OrderHistoryAdapter(context,order.foodArray)
        val layoutManager=LinearLayoutManager(context)
        holder.recyclerHistory.adapter=recyclerAdapter
        holder.recyclerHistory.layoutManager=layoutManager
    }

    override fun getItemCount(): Int {
        return orderArray.size
    }


}