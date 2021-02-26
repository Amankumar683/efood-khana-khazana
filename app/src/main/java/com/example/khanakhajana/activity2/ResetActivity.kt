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

class ResetActivity : AppCompatActivity() {
    lateinit var etOtp: EditText
    lateinit var etPassword: EditText
    lateinit var etConfrmPassword: EditText
    lateinit var btnRetrievePassword: Button
    lateinit var imgBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset)
        etOtp = findViewById(R.id.etOTP)
        etPassword = findViewById(R.id.etPassword)
        etConfrmPassword = findViewById(R.id.etCnfPassword)
        btnRetrievePassword = findViewById(R.id.btnSubmit)
        imgBack = findViewById(R.id.imgBackArrow)
        imgBack.setOnClickListener {
            val intent = Intent(this@ResetActivity, ForgotActivity::class.java)
            startActivity(intent)

        }
        btnRetrievePassword.setOnClickListener {
            val oTp = etOtp.text.toString()
            val mobileNumber = intent.getStringExtra("Number")
            val password = etPassword.text.toString()
            val confmPassword = etConfrmPassword.text.toString()
            if (password.compareTo(confmPassword, false) == 0) {
                val intent = Intent(this@ResetActivity, LogInActivity::class.java)
                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/reset_password/fetch_result"
                val jsonParams = JSONObject().put("mobile_number", mobileNumber).put("password", password).put("otp", oTp)
                if (ConnectionManager().checkConnectivity(this)) {
                    val jsonObjectRequest = object : JsonObjectRequest(
                            Request.Method.POST, url, jsonParams,
                            Response.Listener {
                                try {
                                    val res = it.getJSONObject("data")
                                    val success = res.getBoolean("success")
                                    if (success) {
                                        val trial = res.getString("successMessage")
                                        if (trial.compareTo("Password has successfully changed.", false) == 0) {
                                            Toast.makeText(
                                                    this@ResetActivity,
                                                    "Password has successfully changed.",
                                                    Toast.LENGTH_SHORT
                                            ).show()
                                            startActivity(intent)
                                            finish()

                                        } else {
                                            Toast.makeText(this, "Enter Correct Otp! Please try again", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        Toast.makeText(
                                                this@ResetActivity,
                                                "Enter  new Otp! Please try again",
                                                Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: JSONException) {
                                    Toast.makeText(
                                            this@ResetActivity,
                                            "Some unexpected error occured.",
                                            Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }, Response.ErrorListener {
                        Toast.makeText(
                                this@ResetActivity,
                                "Volley error  occured.",
                                Toast.LENGTH_SHORT
                        ).show()

                    }) {
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
                        finish()
                    }
                    dialog.setNegativeButton("Exit") { text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                    dialog.create().show()
                }

            }
            else {
                Toast.makeText(this, "Passwords don't match!",Toast.LENGTH_SHORT).show()
            }
        }

    }
}
