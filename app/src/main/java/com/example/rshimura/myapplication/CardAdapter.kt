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
import org.w3c.dom.Text


/**
 * Created by rshimura on 2017/07/09.
 */
public class CardAdapter(val context: Context, resourceId: Int, val cardList: List<Card>) :  BaseAdapter() {

    override fun getCount(): Int {
        return cardList.size
    }

    override fun getItemId(position: Int): Long {
        return cardList.lastIndex.toLong()
    }

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
}