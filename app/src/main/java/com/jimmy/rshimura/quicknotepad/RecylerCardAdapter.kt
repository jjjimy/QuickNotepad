package com.jimmy.rshimura.quicknotepad

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
//import com.nineoldandroids.view.ViewHelper
import android.widget.TextView





/**
 * Created by rshimura on 2017/07/09.
 */
public class RecylerCardAdapter(val context: Context, val cardList: List<Card>, val cardLayout: Int):
        RecyclerView.Adapter<RecylerCardAdapter.ViewHolder>() {
    private val inflater: LayoutInflater

    init {
        inflater = LayoutInflater.from(context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cardInfo: Card = getItem(position)
        holder.setItem(cardInfo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecylerCardAdapter.ViewHolder{
        val v: View = inflater.inflate(cardLayout, parent, false)
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

    class ViewHolder(var v: View) : RecyclerView.ViewHolder(v) {
        //public var itemView = v
        private val todo: TextView
        private val date: TextView
        //private val delFil: View
        //private val movFil: View
        //private val arcFil: View
        init {
            todo = v.findViewById(R.id.todo) as TextView
            date = v.findViewById(R.id.date) as TextView
            //delFil = v.findViewById(R.id.delete_mode_fil)
            //movFil = v.findViewById(R.id.move_mode_fil)
            //arcFil = v.findViewById(R.id.archive_mode_fil)
        }

        fun setItem(card: Card){
            todo.text = card.getTodoStr()
            date.text = card.getDateStr()
        }

    }

}