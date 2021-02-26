package com.example.khanakhajana.activity2

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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

class RegisterActivity : AppCompatActivity() {
    lateinit var etname:EditText
    lateinit var etemailAddress:EditText
    lateinit var etmobileNo:EditText
    lateinit var etdeliveryAddress:EditText
    lateinit var etregisterPassword:EditText
    lateinit var etconfirmPassword:EditText
    lateinit var etimageArrow:ImageView
    lateinit var etregisterButton:Button
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        etname=findViewById(R.id.edtxtName)
        etemailAddress=findViewById(R.id.edtxtEmailAddress)
        etmobileNo=findViewById(R.id.edtxtMobileNumber)
        etdeliveryAddress=findViewById(R.id.edtxtDeliveryAddress)
        etregisterPassword=findViewById(R.id.edtxtPassword)
        etconfirmPassword=findViewById(R.id.edtxtConfirmPassword)
        etimageArrow=findViewById(R.id.imgBackArrow)
        etregisterButton=findViewById(R.id.registerButton)
        etimageArrow.setOnClickListener {
            val intent= Intent(this@RegisterActivity,LogInActivity::class.java)
            startActivity(intent)
        }
        etregisterButton.setOnClickListener {
            val name=etname.text.toString()
            val email=etemailAddress.text.toString()
            val mobileNumber=etmobileNo.text.toString()
            val address=etdeliveryAddress.text.toString()
            val password=etregisterPassword.text.toString()
            val intent = Intent(this@RegisterActivity, MainActivity::class.java)
            val queue= Volley.newRequestQueue(this)
            val url="http://13.235.250.119/v2/register/fetch_result"
            val jsonParams= JSONObject().put("name",name).put("mobile_number",mobileNumber).put("password",password).put("address",address)
                    .put("email",email)
            if(ConnectionManager().checkConnectivity(this))
            {
                val jsonObjectRequest=object : JsonObjectRequest(
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
                                            this@RegisterActivity,
                                            "Registration Error! Please try again.",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            catch (e: JSONException) {
                                Toast.makeText(
                                        this@RegisterActivity,
                                        "Some unexpected error occured.",
                                        Toast.LENGTH_SHORT
                                ).show()
                            }

                        }, Response.ErrorListener {
                    Toast.makeText(
                            this@RegisterActivity,
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