package com.jimmy.rshimura.quicknotepad

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.ListView
import java.util.*

/**
 * Created by rshimura on 2017/07/08.
 */
public class LogFragment : Fragment() {


    private var adapter: ArrayAdapter<String>? = null
    private var logView : ListView? = null
    private var itemList: MutableList<String>? = null

    companion object {
        fun getInstance(): Fragment {
            return LogFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View =  inflater.inflate(R.layout.log_fragment, container, false)

        itemList  = mutableListOf()
        adapter   = ArrayAdapter<String>(v?.context, android.R.layout.simple_list_item_1, itemList)
        logView   = v?.findViewById(R.id.log) as ListView

        logView?.adapter = adapter as ListAdapter

        adapter?.notifyDataSetChanged()


        return v
    }

    public fun pushLog(mode: String){
        val date = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance()).toString()
        when (mode){
            "DEL" -> {
                itemList?.add("Delete @$date")
            }
            "CRE" -> {
                itemList?.add("Create @$date")
            }
            "ARC" -> {
                itemList?.add("Archive @$date")
            }
            "REV" -> {
                itemList?.add("Revise @$date")
            }
            "DAR" -> {
                itemList?.add("Dearchive @$date")
            }
        }
        adapter?.notifyDataSetChanged()
    }
}