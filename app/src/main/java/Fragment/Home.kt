package Fragment

import Adapter.HomeAdapter
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.khanakhajana.R
import model.Restaurant
import org.json.JSONException
import util.ConnectionManager
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.collections.HashMap


class Home : Fragment() {
    lateinit var recyclerView: RecyclerView
    var restaurantList=arrayListOf<Restaurant>()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var homeAdapter: HomeAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar: ProgressBar
    var priceComparator= Comparator<Restaurant> { restaurant1,restaurant2 ->
        if(restaurant1.restaurantPrice.compareTo(restaurant2.restaurantPrice,true)==0)
        {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName,true)

        }
        else
        {
            restaurant1.restaurantPrice.compareTo(restaurant2.restaurantPrice,true)
        }
    }
    var ratingComparator= Comparator<Restaurant> { restaurant1,restaurant2 ->
        if(restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating,true)==0)
        {
            restaurant1.restaurantName.compareTo(restaurant2.restaurantName,true)

        }
        else
        {
            restaurant1.restaurantRating.compareTo(restaurant2.restaurantRating,true)
        }
    }
    var nameComparator= Comparator<Restaurant> { restaurant1,restaurant2->
        restaurant1.restaurantName.compareTo(restaurant2.restaurantName,true)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView=view.findViewById(R.id.recyclerHome)
        layoutManager= LinearLayoutManager(context)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)
        progressLayout.visibility=View.VISIBLE
        setHasOptionsMenu(true)
        val queue=Volley.newRequestQueue(activity as Context)
        val url="http://13.235.250.119/v2/restaurants/fetch_result/"
        if(ConnectionManager().checkConnectivity(activity as Context))
            {
                progressLayout.visibility=View.GONE
                val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null,Response.Listener {
                    try {
                        val res=it.getJSONObject("data")
                        val success=res.getBoolean("success")
                        if(success)
                        {
                            val data=res.getJSONArray("data")
                            for (i in 0 until data.length())
                            {
                                val restaurantJsonObject=data.getJSONObject(i)
                                val restaurantObject = Restaurant(
                                    restaurantJsonObject.getString("id"),
                                    restaurantJsonObject.getString("name"),
                                    restaurantJsonObject.getString("rating"),
                                    "₹"+restaurantJsonObject.getString("cost_for_one")+"/person",
                                    restaurantJsonObject.getString("image_url")
                                )
                                restaurantList.add(restaurantObject)
                            }
                            homeAdapter = HomeAdapter(activity as Context, restaurantList)

                            recyclerView.adapter = homeAdapter
                            recyclerView.layoutManager = layoutManager
//                        recyclerHome.addItemDecoration(DividerItemDecoration(recyclerHome.context, (layoutManager as LinearLayoutManager).orientation))
                        } else {
                            Toast.makeText(activity as Context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: JSONException){
                        Toast.makeText(activity as Context, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                    }

                }, Response.ErrorListener {
                    if(activity != null){
                        Toast.makeText(activity as Context, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String> ()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0156ed438db50d"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)

            } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Setting"){text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create().show()
        }




        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_sorting,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id=item.itemId
        if(id==R.id.sortByName)
        {
            Collections.sort(restaurantList,nameComparator)

        }
        else if(id==R.id.sortByRating)
        {
            Collections.sort(restaurantList,ratingComparator)
            restaurantList.reverse()
        }
        else if(id==R.id.sortByPrice)
        {
            Collections.sort(restaurantList,priceComparator)
            restaurantList.reverse()
        }
        homeAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }

}