package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.content.Context
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

    private var cardChangeListener: OnCardChangeListener? = null

    companion object {
        fun getInstance(): Fragment {
            return WriteFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.write_fragment, container, false)

        val itemList: MutableList<Card> = mutableListOf()
        val adapter  = RecylerCardAdapter(v.context, itemList)
        val recyView : RecyclerView = v.findViewById(R.id.todolist) as RecyclerView

        defSwipeAction(v, recyView, adapter, itemList)
        defListAction(v, recyView, adapter, itemList)

        return v
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardChangeListener = context as OnCardChangeListener
    }
    override fun onDetach() {
        super.onDetach()
        cardChangeListener = null
    }


    private fun defListAction(v: View, recyView: RecyclerView,
                              adapter: RecylerCardAdapter, itemList: MutableList<Card>) {
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
                adapter.notifyDataSetChanged()
                cardChangeListener?.onCardCreated()
                inputText.setText("")
            }
            else {
                val card = editCard as Card
                if (card.todo != inputStr){
                    val oldDate = card.getDateStr()
                    card.date = "$oldDate => $currentDate"
                    card.todo = inputStr
                    adapter.notifyDataSetChanged()
                    cardChangeListener?.onCardRevised()
                }
                editCard = null
                inputText.setText("")
                goBtn.setText("Go")
            }
            recyView.scrollToPosition(adapter.getTailIndex())
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
                val card: Card = adapter.getItem(position)
                inputText.setText(card.getTodoStr())
                editCard = card
                goBtn.setText("Re")
            }

            override fun onLongItemClick(view: View, position: Int) {

            }
        }
        recyView.addOnItemTouchListener(RecyclerItemClickListener(v.context, recyView, onItemClickListener))
    }

    private fun defSwipeAction(v: View, recyView: RecyclerView,
                               adapter: RecylerCardAdapter, itemList: MutableList<Card>){

        val layoutMgr: RecyclerView.LayoutManager = LinearLayoutManager(v.context)
        recyView.setHasFixedSize(true)
        recyView.layoutManager = layoutMgr
        recyView.adapter = adapter

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
                        cardChangeListener?.onCardDeleted()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val card = adapter.getItem(position) as Card
                        itemList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        cardChangeListener?.onCardArchived(card)
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

    public interface OnCardChangeListener {

        public fun onCardCreated()

        public fun onCardDeleted()

        public fun onCardRevised()

        public fun onCardArchived(card: Card?)

    }

}

