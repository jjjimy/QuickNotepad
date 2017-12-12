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


    private var adapter : LogAdapter? = null
    private var logItem : LogItem? = null
    private var itemList: MutableList<LogItem>? = null

    companion object {
        fun getInstance(): Fragment {
            return LogFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View =  inflater.inflate(R.layout.log_fragment, container, false)

        itemList  = mutableListOf()
        //adapter   = ArrayAdapter<String>(v?.context, R.layout.log_item_view, itemList)
        adapter   =  LogAdapter(v.context, R.layout.log_item_view, itemList!!)
        var logView:ListView   = v?.findViewById(R.id.log)
        logView?.adapter = adapter
        adapter?.notifyDataSetChanged()


        return v
    }

    public fun pushLog(mode: String){
        val date = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance()).toString()
        val logItem: LogItem = LogItem(mode, date)
        when (mode){
            "DEL" -> {
                itemList?.add(logItem)
            }
            "CRE" -> {
                itemList?.add(logItem)
            }
            "ARC" -> {
                itemList?.add(logItem)
            }
            "REV" -> {
                itemList?.add(logItem)
            }
            "DAR" -> {
                itemList?.add(logItem)
            }
        }
        adapter?.notifyDataSetChanged()
    }
}