package com.example.rshimura.myapplication

import android.content.Context
import android.graphics.Color
import android.graphics.Color.parseColor
import android.support.design.widget.CoordinatorLayout.Behavior.setTag
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
//import com.nineoldandroids.view.ViewHelper
import org.w3c.dom.Text
import android.support.v7.widget.RecyclerView.ViewHolder
import android.widget.TextView






/**
 * Created by rshimura on 2017/07/09.
 */
public class RecylerCardAdapter(val context: Context, val cardList: List<Card>): RecyclerView.Adapter<RecylerCardAdapter.ViewHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardInfo: Card = getItem(position)
        holder.setItem(cardInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecylerCardAdapter.ViewHolder{
        val v: View = inflater.inflate(R.layout.item_view, parent, false)
        val viewholder: ViewHolder = ViewHolder(v)
        return viewholder
    }

     fun getItem(position: Int): Card {
        return cardList[position]
    }
    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun getItemId(position: Int): Long {
        return cardList.lastIndex.toLong()
    }

    fun getTailIndex(): Int {
        return cardList.lastIndex
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private val todo: TextView
        private val date: TextView
        init {
            todo = v.findViewById(R.id.todo) as TextView
            date = v.findViewById(R.id.date) as TextView
        }

        public fun setItem(card: Card){
            todo.text = card.getTodoStr()
            date.text = card.getDateStr()
        }

        public fun setDeleteBg() {

        }


    }




/*
    override fun getItem(position: Int): Card? {
        return cardList[position]
    }

    override  fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val viewHolder: ViewHolder

        if (convertView == null) {
            cv = LayoutInflater.from(context).inflate(R.layout.item_view, null)
            viewHolder = ViewHolder(cv)
            cv!!.tag = viewHolder
        } else {
            viewHolder = cv!!.tag as ViewHolder
        }
        val cardInfo: Card? = getItem(position)
        viewHolder.todo!!.setText(cardInfo!!.getTodoStr())
        viewHolder.date!!.setText(cardInfo!!.getDateStr())

        return cv
    }

    private inner class ViewHolder(view: View) {
        var todo: TextView? = null
        var date: TextView? = null

        init {
            todo = view.findViewById(R.id.todo) as TextView
            date = view.findViewById(R.id.date) as TextView
        }

    }
    */
}