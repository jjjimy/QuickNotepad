package com.jimmy.rshimura.quicknotepad

import android.content.Context
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by rshimura on 2017/07/08.
 */
public class LookFragment : Fragment() {
    private val itemList: MutableList<Card> = mutableListOf()
    private var adapter : RecylerCardAdapter? = null

    private var cardChangeListener: OnArchiveCardChangeListener? = null
    companion object {
        fun getInstance(): Fragment {
            return LookFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.archive_fragment, container, false)
        adapter = RecylerCardAdapter(v.context, itemList)
        var recyView: RecyclerView = v.findViewById(R.id.archivelist) as RecyclerView

        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        recyView?.setHasFixedSize(true)
        recyView?.layoutManager = layoutMgr
        recyView?.adapter = adapter

        defSwipeAction(v, recyView)

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



    public fun archiveThisCard(card: Card?){
        if(card != null){
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            val oldDate = card.getDateStr()
            card.date = "$oldDate => $currentDate"
            itemList.add(card)
            adapter?.notifyDataSetChanged()
        }
    }

    private fun defSwipeAction(v: View, recyView: RecyclerView){
        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        //val adpt: RecylerCardAdapter
        //if (adapter != null){

        //}
        recyView.setHasFixedSize(true)
        recyView.layoutManager = layoutMgr
        recyView.adapter = adapter

        val swipeToActionHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(ItemTouchHelper.DOWN or ItemTouchHelper.UP,  ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {
            /*
            override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
                return  makeFlag(ItemTouchHelper.ACTION_STATE_IDLE,  ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) or
                        makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) or
                        makeFlag(ItemTouchHelper.ACTION_STATE_DRAG,  ItemTouchHelper.UP   or ItemTouchHelper.DOWN)
            }
            */

            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                val fromPos  = viewHolder.adapterPosition
                val toPos    = target.adapterPosition
                adapter?.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val card = adapter?.getItem(position) as Card
                        //view.setDeleteBg()
                        itemList.removeAt(position)
                        adapter?.notifyDataSetChanged()
                        cardChangeListener?.onArchiveCardDearchived(card)
                    }
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        //(viewHolder as RecylerCardAdapter.ViewHolder).setDeleteBg()
                        itemList.removeAt(position)
                        adapter?.notifyDataSetChanged()
                        cardChangeListener?.onArchiveCardDeleted()
                    }

                }
            }
            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                         viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                         actionState: Int, isCurrentlyActive: Boolean) {
            }
        })

        swipeToActionHelper.attachToRecyclerView(recyView)
    }


    public interface OnArchiveCardChangeListener {

        public fun onArchiveCardDearchived(card: Card?)

        public fun onArchiveCardDeleted()


    }

}