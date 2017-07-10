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
import android.text.format.DateFormat
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager


/**
 * Created by rshimura on 2017/07/08.
 */
public class WriteFragment : Fragment() {
    private var v: View? = null
    private var itemList: MutableList<Card>?  = null
    private var listView: EnhancedListView?     = null
    private var adapter : CardAdapter? = null

    companion object {
        fun getInstance(): Fragment {
            return WriteFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        v = inflater.inflate(R.layout.write_fragment, container, false)

        itemList = mutableListOf()
        listView = v!!.findViewById(R.id.todolist) as EnhancedListView
        adapter  = CardAdapter(v!!.context, R.layout.item_view, itemList!!)

        defToDoListView()
        defListItemIO  ()

        return v!!
    }

    private fun defToDoListView() {
        // for todo list view
        listView!!.setAdapter(adapter)
        listView!!.setRequireTouchBeforeDismiss(false)
        listView!!.setUndoHideDelay(3000)

        listView!!.setDismissCallback { listView : EnhancedListView, pos : Int ->
            val item = adapter!!.getItem(pos)
            itemList!!.removeAt(pos)
            adapter!!.notifyDataSetChanged()
            object : EnhancedListView.Undoable() {
                public override fun undo() {
                    itemList!!.add(pos, item!!)
                    adapter!!.notifyDataSetChanged()
                }
            }
        }
        listView!!.enableSwipeToDismiss();
    }

    private fun defListItemIO() {
        // input text and add list
        val inputText: EditText = v!!.findViewById(R.id.inputText) as EditText
        val goBtn    : Button   = v!!.findViewById(R.id.go_btn)    as Button
        var editCard : Card?    = null
        // def btn action
        val pushToDo = {
            when (goBtn.text) {
                "Go" -> {
                    var inputStr = inputText.text.toString()
                    val date = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
                    val inputCard : Card = Card(inputStr, date.toString())
                    itemList!!.add(inputCard)
                    adapter!!.notifyDataSetChanged()
                    inputText.setText("")
                }
                "Re" -> {
                    var inputStr = inputText.text.toString()
                    val date = DateFormat.format("yyyy/MM/dd/kk:mm", Calendar.getInstance())
                    val old  = editCard!!.getDateStr()
                    editCard!!.date = "$old => $date"
                    editCard!!.todo = inputStr
                    adapter!!.notifyDataSetChanged()
                    inputText.setText("")
                    goBtn.setText("Go")
                }
            }
        }

        // edit list
        listView!!.setOnItemClickListener { parent, view, position, id ->
            var item    = parent.getItemAtPosition(position)
            editCard    = item as Card
            inputText.setText(editCard!!.getTodoStr())
            goBtn.setText("Re")
        }
        goBtn.setOnClickListener(View.OnClickListener { pushToDo() })
        // when enter is clicked, push todo
        inputText.setOnKeyListener(object : View.OnKeyListener {

            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {

                if (event.getAction() === KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    pushToDo()
                    return true
                }
                return false
            }
        })
    }

    public interface OnListChangeListener {
        public fun onListChangeListener()
        public fun onListChanged()
    }
}

