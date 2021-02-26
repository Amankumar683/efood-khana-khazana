package com.example.khanakhajana.activity2


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_WIRELESS_SETTINGS
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.khanakhajana.R
import org.json.JSONException
import org.json.JSONObject
import util.ConnectionManager


class LogInActivity : AppCompatActivity() {
    lateinit var emobileNo: EditText
    lateinit var epassword: EditText
    lateinit var fogotPassword: TextView
    lateinit var signIn: TextView
    lateinit var logIn: Button
    lateinit var sharedPreferences:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        val loggedIN=sharedPreferences.getBoolean("loggedIn",false)
        setContentView(R.layout.activity_log_in)

        if (loggedIN) {
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        emobileNo=findViewById(R.id.edtxt1)
        epassword=findViewById(R.id.edtxt2)
        fogotPassword=findViewById(R.id.txtForgotPassword)
        signIn=findViewById(R.id.txtRegister)
        logIn=findViewById(R.id.btLogin)
        logIn.setOnClickListener {
            val mobileNumber=emobileNo.text.toString()
            val password=epassword.text.toString()
            val intent = Intent(this@LogInActivity, MainActivity::class.java)
            val queue= Volley.newRequestQueue(this)
            val url="http://13.235.250.119/v2/login/fetch_result"
            val jsonParams=JSONObject().put("mobile_number",mobileNumber).put("password",password)
            if(ConnectionManager().checkConnectivity(this))
            {
                val jsonObjectRequest=object :JsonObjectRequest(
                    Request.Method.POST,url,jsonParams,
                    Response.Listener {
                        try {
                            val res = it.getJSONObject("data")
                            val success = res.getBoolean("success")
                            if (success) {
                                val data = res.getJSONObject("data")
                                savePreferences(data)
                                startActivity(intent)
                            } else {
                                Toast.makeText(
                                    this@LogInActivity,
                                    "Login Error! Please try again.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        catch (e:JSONException) {
                            Toast.makeText(
                                this@LogInActivity,
                                "Some unexpected error occured.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    },Response.ErrorListener {
                        Toast.makeText(
                            this@LogInActivity,
                            "Volley error  occured.",
                            Toast.LENGTH_SHORT
                        ).show()

                    })
                {
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String,String> ()
                        headers["Content-type"] = "application/json"
                        headers["token"] = "0156ed438db50d"
                        return headers
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else {
                val dialog = AlertDialog.Builder(this)
                dialog.setTitle("ERROR")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("Open Setting"){text, listener ->
                    val settingIntent = Intent(ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create().show()
            }

        }
        fogotPassword.setOnClickListener {
            val intent=Intent(this@LogInActivity,ForgotActivity::class.java)
            startActivity(intent)
        }
        signIn.setOnClickListener {
            val intent=Intent(this@LogInActivity,RegisterActivity::class.java)
            startActivity(intent)
        }



    }
    fun savePreferences(data:JSONObject)
    {
        sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()
        sharedPreferences.edit().putString("user_id",data.getString("user_id")).apply()
        sharedPreferences.edit().putString("name",data.getString("name")).apply()
        sharedPreferences.edit().putString("email",data.getString("email")).apply()
        sharedPreferences.edit().putString("mobile_number",data.getString("mobile_number")).apply()
        sharedPreferences.edit().putString("address",data.getString("address")).apply()
    }
}