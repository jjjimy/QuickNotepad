package com.example.rshimura.myapplication

import android.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.VectorEnabledTintResources
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private val fragMgr: FragmentManager = fragmentManager
    private val listChangeListener = WriteFragment.OnListChangeListener {
        public on
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragMgr.beginTransaction().replace(R.id.fragmentContainer, WriteFragment.getInstance() ).commit()

        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        navigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId){
                R.id.navigation_home -> {
                    fragMgr.beginTransaction().replace(R.id.fragmentContainer, WriteFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    fragMgr.beginTransaction().replace(R.id.fragmentContainer, LookFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    fragMgr.beginTransaction().replace(R.id.fragmentContainer, LogFragment.getInstance() ).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }


    }

}


