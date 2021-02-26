package com.example.khanakhajana.activity2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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

class ForgotActivity : AppCompatActivity() {
    lateinit var edtMobileNo:EditText
    lateinit var edtEmail:EditText
    lateinit var nextButtton:Button
    lateinit var imgBack:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot)
        edtMobileNo=findViewById(R.id.edtForgotPhoneNumber)
        edtEmail=findViewById(R.id.edtForgotEmail)
        nextButtton=findViewById(R.id.btNext)
        imgBack=findViewById(R.id.imgBackArrow)
        imgBack.setOnClickListener {
            val intent=Intent(this@ForgotActivity,LogInActivity::class.java)
            startActivity(intent)

        }
        nextButtton.setOnClickListener {
            val mobileNumber=edtMobileNo.text.toString()
            val email=edtEmail.text.toString()
            val intent = Intent(this@ForgotActivity, ResetActivity::class.java).putExtra("Number",mobileNumber)
            val queue= Volley.newRequestQueue(this)
            val url="http://13.235.250.119/v2/forgot_password/fetch_result"
            val jsonParams= JSONObject().put("mobile_number",mobileNumber).put("email",email)
            if(ConnectionManager().checkConnectivity(this))
            {
                val jsonObjectRequest=object : JsonObjectRequest(
                    Request.Method.POST,url,jsonParams,
                    Response.Listener {
                        try {
                            val res = it.getJSONObject("data")
                            val success = res.getBoolean("success")
                            if (success) {
                                val trial = res.getBoolean("first_try")
                                if(trial) {
                                    Toast.makeText(
                                        this@ForgotActivity,
                                        "OTP sent successfully.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(intent)
                                    finish()

                                }
                                else {
                                    Toast.makeText(this, "Please use the previous OTP provided to registered Email address.", Toast.LENGTH_SHORT).show()
                                    startActivity(intent)
                                    finish()
                                }
                            } else {
                                Toast.makeText(
                                    this@ForgotActivity,
                                    "User not registered.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        catch (e: JSONException) {
                            Toast.makeText(
                                this@ForgotActivity,
                                "Some unexpected error occured.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotActivity,
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
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("Exit"){text, listener ->
                    ActivityCompat.finishAffinity(this)
                }
                dialog.create().show()
            }

        }
        }
    }
