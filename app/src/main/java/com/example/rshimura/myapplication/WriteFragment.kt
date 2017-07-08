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
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager


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

        val itemList: MutableList<String> = mutableListOf()
        val listView: EnhancedListView      = v.findViewById(R.id.todolist) as EnhancedListView
        val adapter : ArrayAdapter<String>  = ArrayAdapter<String>(v.context, android.R.layout.simple_list_item_1, itemList)

        defToDoListView(v, itemList, listView, adapter)
        defListItemIO  (v, itemList, listView, adapter)

        return v
    }

    private fun defToDoListView(v: View, itemList: MutableList<String>,
                                listView: EnhancedListView, adapter: ArrayAdapter<String>): Unit {
        // for todo list view
        listView.setAdapter(adapter)
        listView.setRequireTouchBeforeDismiss(false)
        listView.setUndoHideDelay(3000)

        listView.setDismissCallback { listView : EnhancedListView, pos : Int ->
            val item = adapter.getItem(pos)
            itemList.removeAt(pos)
            adapter.notifyDataSetChanged()
            object : EnhancedListView.Undoable() {
                public override fun undo() {
                    itemList.add(pos, item)
                    adapter.notifyDataSetChanged()
                }
            }
        }
        listView.enableSwipeToDismiss();
    }

    private fun defListItemIO(v : View, itemList: MutableList<String>,
                              listView: EnhancedListView, adapter: ArrayAdapter<String>): Unit {
// input text and add list
        val inputText: EditText = v.findViewById(R.id.inputText) as EditText
        val goBtn    : Button   = v.findViewById(R.id.go_btn)    as Button
        var clickedPosition : Int? = null
        // def btn action
        val pushToDo = {

            when (goBtn.text) {
                "Go" -> {
                    var inputStr = inputText.text.toString()
                    itemList.add(inputStr)
                    adapter.notifyDataSetChanged()
                    inputText.setText("")
                }
                "Re" -> {
                    var inputStr = inputText.text.toString()
                    itemList.set(clickedPosition!!, inputStr)
                    adapter.notifyDataSetChanged()
                    inputText.setText("")
                    goBtn.setText("Go")
                }
            }
        }

        // edit list
        listView.setOnItemClickListener { parent, view, position, id ->
            val item    = parent.getItemAtPosition(id.toInt())
            val editStr = item.toString()
            clickedPosition = position
            inputText.setText(editStr)
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
}