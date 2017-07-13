package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

/**
 * Created by rshimura on 2017/07/08.
 */
public class LogFragment : Fragment() {

    var v: View? = null
    private var adapter: ArrayAdapter<String>? = null
    private var logView : ListView? = null
    private var itemList: MutableList<String>? = null

    companion object {
        fun getInstance(): Fragment {
            return LogFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        v =  inflater.inflate(R.layout.log_fragment, container, false)

        itemList  = mutableListOf()
        adapter   = ArrayAdapter<String>(v?.context, android.R.layout.simple_list_item_1, itemList)
        logView   = v?.findViewById(R.id.log) as ListView

        logView?.adapter = adapter


        return inflater.inflate(R.layout.log_fragment, container, false)
    }

    public fun pushLog(mode: String){
        when (mode){
            "DEL" -> {
                itemList?.add("delete")
                adapter?.notifyDataSetChanged()
            }
        }
    }
}