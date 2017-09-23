package com.example.rshimura.myapplication

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
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
    private var cardChangeListener: OnArchiveCardChangeListener? = null
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


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardChangeListener = context as OnArchiveCardChangeListener
    }

    override fun onDetach() {
        super.onDetach()
        cardChangeListener = null
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when(direction) {
            ItemTouchHelper.LEFT -> {
                val position = viewHolder.adapterPosition
                val view = viewHolder as RecylerCardAdapter.ViewHolder
                //view.setDeleteBg()
                itemList.removeAt(position)
                adapter.notifyDataSetChanged()
                cardChangeListener?.onWriteCardDeleted()
            }
            ItemTouchHelper.RIGHT -> {
                val position = viewHolder.adapterPosition
                val card = adapter.getItem(position) as Card
                //(viewHolder as RecylerCardAdapter.ViewHolder).setDeleteBg()
                itemList.removeAt(position)
                adapter.notifyDataSetChanged()
                cardChangeListener?.onWriteCardArchived(card)
            }

        }
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

    public interface OnArchiveCardChangeListener {

        public fun onArchiveCardDearchived()

        public fun onArchiveCardDeleted()


    }

}