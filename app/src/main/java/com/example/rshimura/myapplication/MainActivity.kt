package com.example.rshimura.myapplication

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private var mTextMessage: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

                val navigation = findViewById(R.id.navigation) as BottomNavigationView
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.setOnNavigationItemSelectedListener { item ->
            val fragManager = fragmentManager;
            when (item.itemId){
                R.id.navigation_home -> {
                    fragManager.beginTransaction().replace(R.id.fragmentContainer, WriteFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    fragManager.beginTransaction().replace(R.id.fragmentContainer, LookFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    fragManager.beginTransaction().replace(R.id.fragmentContainer, LogFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

}
