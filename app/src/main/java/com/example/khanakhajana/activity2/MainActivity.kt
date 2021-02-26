package com.example.khanakhajana.activity2

import Fragment.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.khanakhajana.R
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerLayout:DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousItem:MenuItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file), Context.MODE_PRIVATE)
        drawerLayout=findViewById(R.id.drawerLayout)
        coordinatorLayout=findViewById(R.id.coordinatorLayout)
        toolbar=findViewById(R.id.toolBar)
        frameLayout=findViewById(R.id.frameLayout)
        navigationView=findViewById(R.id.navigationView)
        setSupportActionBar(toolbar)
        supportActionBar?.title="All restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        openHome()
        val actionBarToggle=ActionBarDrawerToggle(this,drawerLayout,R.string.openDrawer,R.string.closeDrawer)
        drawerLayout.addDrawerListener(actionBarToggle)
        actionBarToggle.syncState()
        navigationView.setNavigationItemSelectedListener {
            if (previousItem != null)
                previousItem?.isChecked = false
            it.isCheckable = true
            it.isChecked = true
            previousItem = it
            when(it.itemId)
            {
                R.id.homeId -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }
                R.id.MyProfile->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,ProfileFragment()).commit()
                    supportActionBar?.title="My profile"
                    drawerLayout.closeDrawers()
                }
                R.id.favourateRestaurants->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,FavourateFragment()).commit()
                    supportActionBar?.title="Favourates"
                    drawerLayout.closeDrawers()
                }
                R.id.orderHistory->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,OrderFragment()).commit()
                    supportActionBar?.title="Order history"

                    drawerLayout.closeDrawers()
                }
                R.id.faqs->
                {
                    supportFragmentManager.beginTransaction().replace(R.id.frameLayout,FaqFragment()).commit()
                    supportActionBar?.title="FAQs"
                    drawerLayout.closeDrawers()
                }
                R.id.logOut->
                {
                    val dialog=AlertDialog.Builder(this)
                    dialog.setTitle("Log out")
                    dialog.setMessage("Do you want to logout?")
                    dialog.setPositiveButton("yes"){text,listener->
                        sharedPreferences.edit().clear().apply()
                        val intent= Intent(this@MainActivity,LogInActivity::class.java)
                        startActivity(intent)

                    }
                    dialog.setNegativeButton("No"){text,listener->

                    }
                    dialog.create().show()

                }







            }
            
        return@setNavigationItemSelectedListener true


        }
    }
    fun openHome(){
        val fragment =
            Home()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout,fragment).commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.homeId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }
}