package com.example.khanakhajana.activity2

import Adapter.DescriptionAdapter
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.khanakhajana.R
import model.Dish
import org.json.JSONException
import util.ConnectionManager
import java.util.ArrayList




class DescriptionActivity : AppCompatActivity() {
    var restaurantId: String? = "0"
    var restaurantName: String? = "0"
    lateinit var recyclerDescription: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var descriptionAdapter: DescriptionAdapter
    lateinit var progressLayout2: RelativeLayout
    lateinit var toolbar: Toolbar
    lateinit var progressBar2: ProgressBar
    var dishList:ArrayList<Dish> = arrayListOf()
    var orderList:ArrayList<Dish> = arrayListOf()
    lateinit var proceedToCart: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        toolbar = findViewById(R.id.toolBar2)
        recyclerDescription = findViewById(R.id.recyclerDiscription)
        layoutManager = LinearLayoutManager(this)
        progressLayout2 = findViewById(R.id.progressLayout2)
        progressBar2 = findViewById(R.id.progessBar2)
        progressLayout2.visibility = View.VISIBLE
        proceedToCart = findViewById(R.id.btnProceedToCart)
        if (intent != null) {
            restaurantId = intent.getStringExtra("id")!!
            restaurantName = intent.getStringExtra("name")!!
            toolbar.title = intent.getStringExtra("name")

        } else {
            finish()
            Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
        }
        if (restaurantId == "0") {
            finish()
            Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
        }
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"
        if (ConnectionManager().checkConnectivity(this)) {
            progressLayout2.visibility = View.GONE
            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                try {
                    val res = it.getJSONObject("data")
                    val success = res.getBoolean("success")
                    if (success) {
                        val data = res.getJSONArray("data")
                        for (i in 0 until data.length()) {
                            val dishObject = data.getJSONObject(i)
                            val dishNameObject = Dish(dishObject.getString("id"), dishObject.getString("name"), dishObject.getString("cost_for_one"))
                            dishList.add(dishNameObject)
                        }
                        descriptionAdapter = DescriptionAdapter(this, dishList, orderList)
                        recyclerDescription.adapter = descriptionAdapter
                        recyclerDescription.layoutManager = layoutManager
                    } else {
                        Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
            }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "0156ed438db50d"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(this)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Setting") { text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                this.finish()
            }
            dialog.setNegativeButton("Exit") { text, listener ->
                ActivityCompat.finishAffinity(this)
            }
            dialog.create().show()
        }
        proceedToCart.setOnClickListener {
            if (orderList.size>0) {
                val intent = Intent(this, CartActivity::class.java)
                    intent.putParcelableArrayListExtra("order",orderList).putExtra("res",restaurantName)
                    startActivity(intent)
            } else {
                Toast.makeText(this, "Select atleast one dish on menu!", Toast.LENGTH_SHORT).show()
            }

        }


    }

}





