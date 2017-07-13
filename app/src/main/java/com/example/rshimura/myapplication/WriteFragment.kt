package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import de.timroes.android.listview.EnhancedListView
import java.util.*
import android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN
import android.view.KeyEvent.KEYCODE_ENTER
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Canvas
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.support.v7.widget.RecyclerView




/**
 * Created by rshimura on 2017/07/08.
 */
public class WriteFragment : Fragment() {

    companion object {
        fun getInstance(): Fragment {
            return WriteFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.write_fragment, container, false)

        val itemList: MutableList<Card> = mutableListOf()
        //val listView = v.findViewById(R.id.todolist) as EnhancedListView
        val adapter  = RecylerCardAdapter(v.context, itemList)

        //defToDoListView(v, listView, adapter, itemList)
        //defListItemIO  (v, listView, adapter, itemList)


        val recyView : RecyclerView = v.findViewById(R.id.todolist) as RecyclerView
        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        recyView.setHasFixedSize(true)
        recyView.layoutManager = layoutMgr
        recyView.adapter = adapter
        defListItemIO(v, recyView, adapter, itemList)


        val swipeToActionHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        itemList.removeAt(position)
                        adapter.notifyDataSetChanged()
                    }

                }
            }

            override fun onChildDrawOver(c: Canvas, recyclerView: RecyclerView,
                                         viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float,
                                         actionState: Int, isCurrentlyActive: Boolean) {
            }
        })

        swipeToActionHelper.attachToRecyclerView(recyView)

        return v
    }

    private fun defListItemIO(v: View, recyView: RecyclerView,
                              adapter: RecylerCardAdapter, itemList: MutableList<Card>) {
        // input text and add list
        val inputText: EditText = v.findViewById(R.id.inputText) as EditText
        val goBtn: Button = v.findViewById(R.id.go_btn) as Button
        // def btn action

        val pushAction = { card: Card? ->
            val inputStr = inputText.text.toString()
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            if (card == null){
                val inputCard: Card = Card(inputStr, currentDate.toString())
                itemList.add(inputCard)
                adapter.notifyDataSetChanged()
                inputText.setText("")
            }
            else {
                if (card.todo != inputStr){
                    val oldDate = card.getDateStr()
                    card.date = "$oldDate => $currentDate"
                    card.todo = inputStr
                    adapter.notifyDataSetChanged()
                }
                inputText.setText("")
                goBtn.setText("Go")
            }
        }

        val onKeyListener = { card: Card? ->
            object : View.OnKeyListener {
                override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                    if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        pushAction(card)
                        return true
                    }
                    return false
                }
            }
        }


        // add list
        goBtn.setOnClickListener { pushAction(null) }
        inputText.setOnKeyListener(onKeyListener(null))
        // edit list
        val onItemClickListener = object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                val editCard: Card = adapter.getItem(position)
                inputText.setText(editCard.getTodoStr())
                goBtn.setText("Re")
                goBtn.setOnClickListener   { pushAction(editCard) }
                inputText.setOnKeyListener(onKeyListener(editCard))
            }
            override fun onLongItemClick(view: View, position: Int) {
            }
        }
        recyView.addOnItemTouchListener(RecyclerItemClickListener(v.context, recyView, onItemClickListener))
    }

    /*
    private fun defToDoListView(v: View, listView: EnhancedListView,
                                adapter: CardAdapter, itemList: MutableList<Card>) {
        // for todo list view
        listView.setAdapter(adapter)
        listView.setRequireTouchBeforeDismiss(false)
        listView.setUndoHideDelay(3000)

        listView.setDismissCallback { lv : EnhancedListView, pos : Int ->
            val item = adapter.getItem(pos)
            itemList.removeAt(pos)
            adapter.notifyDataSetChanged()
            object : EnhancedListView.Undoable() {
                public override fun undo() {
                    itemList.add(pos, item!!)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        listView.enableSwipeToDismiss();
    }

        */

}

