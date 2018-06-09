package com.jimmy.rshimura.quicknotepad

import android.os.Bundle
import android.support.v4.app.Fragment
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import java.util.*
import android.graphics.Canvas
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.util.Log
import android.view.KeyEvent
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import android.support.design.widget.SwipeDismissBehavior
import android.support.design.widget.CoordinatorLayout;








/**
 * Created by rshimura on 2017/07/08.
 */
public class WriteFragment : Fragment() {

    private var cardChangeListener: OnWriteCardChangeListener? = null
    private var itemList: MutableList<Card>  = mutableListOf()
    private var adapter : RecylerCardAdapter? = null

    companion object {
        fun getInstance(): Fragment {
            return WriteFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.write_fragment, container, false)

        adapter  = RecylerCardAdapter(v.context, itemList, R.layout.item_view)
        val recyView : RecyclerView = v.findViewById(R.id.todolist) as RecyclerView

        defSwipeAction(v, recyView)
        defListAction(v,  recyView)

        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardChangeListener = context as OnWriteCardChangeListener
    }
    override fun onDetach() {
        super.onDetach()
        cardChangeListener = null
    }


    private fun defListAction(v: View, recyView: RecyclerView) {
        // input text and add list
        val inputText: EditText = v.findViewById(R.id.inputText) as EditText
        val goBtn: Button = v.findViewById(R.id.go_btn) as Button
        var editCard: Card? = null
        // def btn action

        var pushAction = {
            val inputStr = inputText.text.toString()
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            if (editCard == null){
                val inputCard: Card = Card(inputStr, currentDate.toString())
                itemList.add(inputCard)
                adapter?.notifyDataSetChanged()
                cardChangeListener?.onWriteCardCreated()
                inputText.setText("")
            }
            else {
                val card = editCard as Card
                if (card.todo != inputStr){
                    card.todo = inputStr
                    adapter?.notifyDataSetChanged()
                    cardChangeListener?.onWriteCardRevised()
                    card.date = currentDate.toString()
                }
                editCard = null
                inputText.setText("")
                //goBtn.setText("Go")
            }
            recyView.scrollToPosition(adapter?.getTailIndex()!!)
        }

        val onKeyListener = { card: Card? ->
            object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        pushAction()
                        return true
                    }
                    return false
                }
            }
        }


        // add list
        goBtn.setOnClickListener { pushAction(); Log.d("ONCLICK", "yeeah") }
        inputText.setOnKeyListener(onKeyListener(null))
        // edit list
        val onItemClickListener = object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val card: Card? = adapter?.getItem(position)
                inputText.setText(card?.getTodoStr())
                editCard = card
                //goBtn.setText("Re")
            }

            override fun onLongItemClick(view: View, position: Int) {
                val card = adapter?.getItem(position) as Card

            }
        }
        recyView.addOnItemTouchListener(RecyclerItemClickListener(v.context, recyView, onItemClickListener))
    }

    private fun defSwipeAction(v: View, recyView: RecyclerView){

        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        recyView.setHasFixedSize(true)
        recyView.layoutManager = layoutMgr
        recyView.adapter = adapter

        // implement swipe action
        val swipeToActionHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.DOWN or ItemTouchHelper.UP,
                ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT) {

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
                        val view = viewHolder as RecylerCardAdapter.ViewHolder
                        //view.setDeleteBg()
                        itemList.removeAt(position)
                        adapter?.notifyDataSetChanged()
                        cardChangeListener?.onWriteCardDeleted()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val card = adapter?.getItem(position) as Card
                        //(viewHolder as RecylerCardAdapter.ViewHolder).setDeleteBg()
                        itemList.removeAt(position)
                        adapter?.notifyDataSetChanged()
                        cardChangeListener?.onWriteCardArchived(card)
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


    public fun deArchiveThisCard(card: Card?){
        if(card != null){
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            val oldDate = card.getDateStr()
            card.date = "$currentDate"
            itemList.add(card)
            adapter?.notifyDataSetChanged()
        }
    }

    public interface OnWriteCardChangeListener {

        public fun onWriteCardCreated()

        public fun onWriteCardDeleted()

        public fun onWriteCardRevised()

        public fun onWriteCardArchived(card: Card?)

    }


}

