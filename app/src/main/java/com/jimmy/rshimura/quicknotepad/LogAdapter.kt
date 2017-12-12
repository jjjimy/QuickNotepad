package com.jimmy.rshimura.quicknotepad

import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.xmlpull.v1.XmlPullParser


/**
 * Created by rshimura on 2017/12/09.
 */
public class LogAdapter(val context: Context, val resourceId: Int, val logItemList: List<LogItem>) :  BaseAdapter() {

    override fun getCount(): Int {
        return logItemList.size
    }

    override fun getItemId(position: Int): Long {
        return logItemList.lastIndex.toLong()
    }

    override fun getItem(position: Int): LogItem? {
        return logItemList[position]
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var cv = convertView
        val viewHolder: ViewHolder

        if (convertView == null) {
            cv = LayoutInflater.from(context).inflate(resourceId, null)
            viewHolder = ViewHolder(cv)
            cv!!.tag = viewHolder
        } else {
            viewHolder = cv!!.tag as ViewHolder
        }
        val logItem: LogItem? = getItem(position)
        when (logItem!!.mode) {
            "DEL" -> {
                viewHolder.modeImage!!.setBackgroundResource(R.drawable.log_icon_remove)
            }
            "CRE" -> {
                viewHolder.modeImage!!.setBackgroundResource(R.drawable.log_icon_memo)
            }
            "ARC" -> {
                viewHolder.modeImage!!.setBackgroundResource(R.drawable.log_icon_archive)
            }
            "REV" -> {
                viewHolder.modeImage!!.setBackgroundResource(R.drawable.log_icon_edit)
            }
            "DAR" -> {
                viewHolder.modeImage!!.setBackgroundResource(R.drawable.log_icon_dearchive)
            }
        }
        viewHolder.logText!!.setText(logItem!!.getLogText())

        return cv
    }

    private inner class ViewHolder(view: View) {
        var modeImage: ImageView? = null
        var logText: TextView? = null

        init {
            modeImage = view.findViewById(R.id.mode_mark_image)
            logText = view.findViewById(R.id.log_text)
        }

    }
}
