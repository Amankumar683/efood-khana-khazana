package Adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khanakhajana.R
import model.Dish


class DescriptionAdapter(val context: Context,val dishArray:ArrayList<Dish>,val orderArray:ArrayList<Dish>):RecyclerView.Adapter<DescriptionAdapter.DescriptionViewHolder>() {

    class DescriptionViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val dishId: TextView = view.findViewById(R.id.txtNumber)
        val dishName: TextView = view.findViewById(R.id.txtDishName)
        val dishPrice: TextView = view.findViewById(R.id.txtDishPrice)
        val dishAddRemoveButton: Button = view.findViewById(R.id.btnAddRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_discription, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val dish = dishArray[position]
        val temp = (position + 1).toString() + "."
        val tempString = "₹" + dish.dishPrice
        holder.dishId.text = temp
        holder.dishName.text = dish.dishName
        holder.dishPrice.text = tempString
        checkBtn(dish, holder)
        holder.dishAddRemoveButton.setOnClickListener {
            if(dish in orderArray)
                orderArray.remove(dish)
            else
                orderArray.add(dish)
            checkBtn(dish,holder)

        }


    }

    override fun getItemCount(): Int {
        return dishArray.size
    }

    fun checkBtn(dish: Dish, holder: DescriptionViewHolder) {
        if (dish in orderArray) {
            holder.dishAddRemoveButton.text="Remove"
            holder.dishAddRemoveButton.setBackgroundColor(Color.parseColor("#EE6C00"))

        }
        else
        {
            holder.dishAddRemoveButton.text="Add"
            holder.dishAddRemoveButton.setBackgroundColor(Color.parseColor("#FFFFAB00"))
        }

    }
}