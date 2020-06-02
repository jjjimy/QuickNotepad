package com.jimmy.rshimura.quicknotepad

import android.support.v4.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.SearchView


class MainActivity() : AppCompatActivity(){

    private val fragMgr: FragmentManager = supportFragmentManager
    private val writeFrag = WriteFragment()
    private val archFrag = LookFragment()
    private val logFrag   = LogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Toolbar setting
        val myToolbar = findViewById(R.id.my_toolbar) as Toolbar
        setSupportActionBar(myToolbar)
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        fragMgr.beginTransaction().add(R.id.fragmentContainer, writeFrag, "write")
                                  .add(R.id.fragmentContainer, archFrag, "look")
                                  .add(R.id.fragmentContainer, logFrag  , "log")
                                  .hide(archFrag)
                                  .hide(logFrag)
                                  .commit()

        navigation.setOnNavigationItemSelectedListener { item ->
            if(writeFrag.isVisible){
                fragMgr.beginTransaction().hide(writeFrag).commit()
            }
            if(archFrag.isVisible){
                fragMgr.beginTransaction().hide(archFrag).commit()
            }
            if(logFrag.isVisible){
                fragMgr.beginTransaction().hide(logFrag).commit()
            }
            when (item.itemId){
                R.id.navigation_home -> {
                    fragMgr.beginTransaction().show(writeFrag).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    fragMgr.beginTransaction().show(archFrag).commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    fragMgr.beginTransaction().show(logFrag).commit()
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }

        //implement onCardChangeListener
        writeFrag.setOnWriteCardChangeListener(object: WriteFragment.OnWriteCardChangeListener {
            // writeFragment Event
            override fun onWriteCardCreated() {
                Log.d("ONLISTCHANGE", "CRE")
                logFrag.pushLog("CRE")
            }
            override fun onWriteCardDeleted() {
                Log.d("ONLISTCHANGE", "DEL")
                logFrag.pushLog("DEL")
            }

            override fun onWriteCardRevised() {
                logFrag.pushLog("REV")
            }

            override fun onWriteCardArchived(card: Card?) {
                archFrag.archiveThisCard(card)
                Log.d("ONLISTCHANGE", "ARC")
                logFrag.pushLog("ARC")
            }
        })
        archFrag.setOnArchiveCardChangeListener(object: LookFragment.OnArchiveCardChangeListener{
            // archiveFragment Event
            override fun onArchiveCardDearchived(card: Card?) {
                writeFrag.deArchiveThisCard(card)
                logFrag.pushLog("DAR")
            }

            override fun onArchiveCardDeleted() {
                logFrag.pushLog("DEL")
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.actionbar, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchItem) as SearchView
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_settings ->
                // User chose the "Settings" item, show the app settings UI...
                return true

            R.id.action_search ->
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                return true

            else ->
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item)
        }
    }




}


