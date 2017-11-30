package com.jimmy.rshimura.quicknotepad

import android.support.v4.app.FragmentManager
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.util.Log



class MainActivity() : AppCompatActivity(), WriteFragment.OnWriteCardChangeListener, LookFragment.OnArchiveCardChangeListener {





    private val fragMgr: FragmentManager = supportFragmentManager
    private val writeFrag = WriteFragment()
    private val archFrag = LookFragment()
    private val logFrag   = LogFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navigation = findViewById(R.id.navigation) as BottomNavigationView
        fragMgr.beginTransaction().add(R.id.fragmentContainer, writeFrag, "write")
                                  .add(R.id.fragmentContainer, archFrag, "look")
                                  .add(R.id.fragmentContainer, logFrag  , "log")
                                  .hide(archFrag)
                                  .hide(logFrag)
                                  .commit()
        /*
        /* タイトル */
        getSupportActionBar().setTitle("タイトル")

        /* ロゴ画像を出す */
        getSupportActionBar().setDisplayShowHomeEnabled(true)
        getSupportActionBar().setDisplayUseLogoEnabled(true)
        /* アイコン変える */
        getSupportActionBar().setLogo(R.drawable.ic_launcher)
        */

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
    }

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

    // archiveFragment Event
    override fun onArchiveCardDearchived(card: Card?) {
        writeFrag.deArchiveThisCard(card)
        logFrag.pushLog("DAR")
    }

    override fun onArchiveCardDeleted() {
        logFrag.pushLog("DEL")
    }
}


