package com.jimmy.rshimura.quicknotepad

import android.graphics.Canvas
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.format.DateFormat
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import java.util.*


//TODO: 他のところをさわっと時の挙動
//TODO: FABを３色ボタンに展開
//TODO: BottomSheetの代わりにBottomDialog
//TODO: keyboardを閉じた時に、BottomSheetを閉じる。

/**
 * Created by rshimura on 2017/07/08.
 */
public class WriteFragment : Fragment() {

    // get listener/list/adapter instance as class var with NULL.
    private lateinit var cardChangeListener: OnWriteCardChangeListener
    private var itemList: MutableList<Card>  = mutableListOf()
    private lateinit var adapter : RecylerCardAdapter
    private lateinit var bottomSheet: View
    private lateinit var behavior: BottomSheetBehavior<View>

    companion object {
        fun getInstance(): Fragment {
            return WriteFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val v: View = inflater.inflate(R.layout.write_fragment, container, false)

        adapter  = RecylerCardAdapter(v.context, itemList, R.layout.item_view)
        val recyView : RecyclerView = v.findViewById(R.id.todolist) as RecyclerView


        // bottom sheet
        bottomSheet = v.findViewById(R.id.bottom_sheet)
        behavior = BottomSheetBehavior.from(bottomSheet)
        behavior.isHideable = true
        behavior.state = BottomSheetBehavior.STATE_HIDDEN

        defFabAction(v)
        defSwipeAction(v, recyView)
        defListAction(v,  recyView)

        return v
    }
    /*
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        cardChangeListener = context as OnWriteCardChangeListener
    }
    */
    override fun onDetach() {
        super.onDetach()
        // 20.6.4 by rshimura
        // I think cardChangeListener shouldn't be null,
        // and comment out the follow line, but comment in if some problem occur
        //cardChangeListener = null
    }

    private fun defFabAction(v: View) {
        // config floating button
        val cFab: View = v.findViewById(R.id.createFloating)
        cFab.setOnClickListener { view: View ->
            Log.d("FAB", behavior.state.toString())
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                Log.d("FAB", behavior.state.toString())
            } else {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                Log.d("FAB", behavior.state.toString())
            }
        }
    }

    private fun defListAction(v: View, recyView: RecyclerView) {
        // input text and add list
        val inputText: EditText = v.findViewById(R.id.inputText) as EditText
        val goBtn: Button = v.findViewById(R.id.go_btn) as Button
        var editCard: Card? = null
        // def btn action

        val pushAction = { v: View ->
            val inputStr = inputText?.text.toString()
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            // check if is the inputStr empty ?
            if (inputStr != "") {
                if (editCard == null) {
                    val inputCard: Card = Card(inputStr, currentDate.toString())
                    itemList.add(inputCard)
                    adapter.notifyDataSetChanged()
                    cardChangeListener.onWriteCardCreated()
                    inputText?.setText("")
                } else {
                    val card = editCard as Card
                    if (card.todo != inputStr) {
                        card.todo = inputStr
                        adapter.notifyDataSetChanged()
                        cardChangeListener.onWriteCardRevised()
                        card.date = currentDate.toString()
                    }
                    editCard = null
                    inputText?.setText("")
                    //goBtn.setText("Go")
                }
                recyView.scrollToPosition(adapter.getTailIndex()!!)
            }
        }


        val enterAction = l@{ v: View, keyCode: Int, event: KeyEvent ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    pushAction
                    return@l true
                }
                false
        }

        // add list
        goBtn?.setOnClickListener(pushAction)
        inputText?.setOnKeyListener(enterAction)
        // edit list
        val onItemClickListener = object : RecyclerItemClickListener.OnItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                val card: Card? = adapter.getItem(position)
                inputText?.setText(card?.getTodoStr())
                editCard = card
                //goBtn.setText("Re")
            }

            override fun onLongItemClick(view: View, position: Int) {
                val card = adapter.getItem(position) as Card
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

            override fun onMove(recyclerView: RecyclerView,
                                viewHolder: RecyclerView.ViewHolder,
                                target: RecyclerView.ViewHolder): Boolean {
                val fromPos  = viewHolder.adapterPosition
                val toPos    = target.adapterPosition
                adapter.notifyItemMoved(fromPos, toPos)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when(direction) {
                    ItemTouchHelper.LEFT -> {
                        val position = viewHolder.adapterPosition
                        val view = viewHolder as RecylerCardAdapter.ViewHolder
                        //view.setDeleteBg()
                        itemList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        cardChangeListener.onWriteCardDeleted()
                    }
                    ItemTouchHelper.RIGHT -> {
                        val position = viewHolder.adapterPosition
                        val card = adapter.getItem(position) as Card
                        //(viewHolder as RecylerCardAdapter.ViewHolder).setDeleteBg()
                        itemList.removeAt(position)
                        adapter.notifyDataSetChanged()
                        cardChangeListener.onWriteCardArchived(card)
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


    fun deArchiveThisCard(card: Card?){
        if(card != null){
            val currentDate = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
            val oldDate = card.getDateStr()
            card.date = "$currentDate"
            itemList.add(card)
            adapter?.notifyDataSetChanged()
        }
    }

    fun setOnWriteCardChangeListener (listener: OnWriteCardChangeListener){
        cardChangeListener = listener
    }

    interface OnWriteCardChangeListener {

        fun onWriteCardCreated()

        fun onWriteCardDeleted()

        fun onWriteCardRevised()

        fun onWriteCardArchived(card: Card?)

    }


}

