package com.example.khanakhajana.activity2

import Adapter.CartAdapter
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.media.audiofx.BassBoost
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_WIRELESS_SETTINGS
import androidx.appcompat.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.khanakhajana.R
import model.Dish
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import util.ConnectionManager

class CartActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var cartAdapter: CartAdapter
    lateinit var btnPlaceOrder: Button
    lateinit var orderFrom: TextView
    lateinit var confirmScreen: RelativeLayout
    lateinit var confirmBtn: Button
    lateinit var sharedPreferences: SharedPreferences
    var total = 0
    lateinit var foodArray: JSONArray
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        recyclerView = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this)
        confirmBtn = findViewById(R.id.btnProceedToMain)
        orderFrom = findViewById(R.id.orderRestaurant)
        confirmScreen = findViewById(R.id.confirmLayout)
        btnPlaceOrder = findViewById(R.id.btnProceOrder)
        foodArray = JSONArray()
        if (intent != null) {
            val orderArray = intent.getParcelableArrayListExtra<Dish>("order")
            val restaurantName = "Ordering From:" + intent.getStringExtra("res")
            orderFrom.text = restaurantName
            if (orderArray != null) {
                for (i in orderArray) {
                    total += i.dishPrice?.toInt() ?: 0
                    val obj = JSONObject().put("food_item_id", i.dishId)
                    foodArray.put(obj)


                }
            }
            val temp = "Place Order(Total ₹$total)"
            btnPlaceOrder.text = temp
            cartAdapter = CartAdapter(this, orderArray)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = cartAdapter
            btnPlaceOrder.setOnClickListener {
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                val params =
                    JSONObject().put("user_id", sharedPreferences.getString("user_id", "0")).put(
                        "restaurant_id",
                        orderArray?.get(0)?.dishId
                    ).put("total_cost", total).put("food", foodArray)
                if (ConnectionManager().checkConnectivity(this)) {

                    val jsonObjectRequest =
                        object : JsonObjectRequest(Method.POST, url, params, Response.Listener {

                            try {
                                val res = it.getJSONObject("data")
                                val success = res.getBoolean("success")
                                if (success) {
                                    confirmScreen.visibility = View.VISIBLE
                                    btnPlaceOrder.visibility = View.GONE

                                } else {
                                    Toast.makeText(
                                        this,
                                        "Error in placing order! Please try again.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this,
                                    "Some unexpected error occurred!!!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }, Response.ErrorListener {
                            Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT)
                                .show()
                        }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "64dd3ad5877c86"
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
                        finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                    dialog.create().show()
                }
            }
        }

        confirmBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}