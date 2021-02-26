package Fragment

import Adapter.FavourateAdapter
import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.khanakhajana.R
import database.RestaurantDatabase
import database.RestaurantEntity


class FavourateFragment : Fragment() {
    lateinit var recyclerView: RecyclerView
    lateinit var adapter:FavourateAdapter
    var itemArray= listOf<RestaurantEntity>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_favourate, container, false)
        recyclerView=view.findViewById(R.id.recyclerFavourate)
        layoutManager= LinearLayoutManager(activity as Context)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        itemArray=Retrieve(activity as Context).execute().get()
        if(activity!=null)
        {
            progressLayout.visibility=View.GONE
            adapter= FavourateAdapter(activity as Context,itemArray)

            recyclerView.layoutManager=layoutManager

            recyclerView.adapter=adapter


        }
        return view
    }
    class Retrieve(val context: Context):AsyncTask<Void,Void,List<RestaurantEntity>>()
    {

        override fun doInBackground(vararg params: Void?): List<RestaurantEntity> {
            val db= Room.databaseBuilder(context,RestaurantDatabase::class.java,"restaurants-db").build()
            return db.restaurantdao().getAllRestaurants()



        }

    }



}