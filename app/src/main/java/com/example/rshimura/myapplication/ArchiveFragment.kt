package com.example.rshimura.myapplication

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by rshimura on 2017/07/08.
 */
public class LookFragment : Fragment() {
    private val itemList: MutableList<Card> = mutableListOf()
    private var adapter : RecyclerView.Adapter<RecylerCardAdapter.ViewHolder>? = null
    private var recyView: RecyclerView? = null
    companion object {
        fun getInstance(): Fragment {
            return LookFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.archive_fragment, container, false)

        adapter  = RecylerCardAdapter(v.context, itemList)
        recyView = v.findViewById(R.id.archivelist) as RecyclerView

        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        recyView?.setHasFixedSize(true)
        recyView?.layoutManager = layoutMgr
        recyView?.adapter = adapter

        return v
    }

    public fun archiveThisCard(card: Card?){
        if(card != null){

            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            val oldDate = card.getDateStr()
            card.date = "$oldDate => $currentDate"
            itemList.add(card)
            adapter?.notifyDataSetChanged()
        }
    }
}