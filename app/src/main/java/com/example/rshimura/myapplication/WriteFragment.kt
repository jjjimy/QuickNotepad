package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import de.timroes.android.listview.EnhancedListView
import java.util.*

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
        val v : View = inflater.inflate(R.layout.write_fragment, container, false)

        // for todo list view
        var itemList : LinkedList<String>    = emptyList<String>() as LinkedList<String>
        val listView : EnhancedListView      = v.findViewById<EnhancedListView>(R.id.todolist)
        val adapter  : ArrayAdapter<String>  = ArrayAdapter<String>(v.context, R.layout.todo_list, itemList)
        listView.setAdapter(adapter)
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
        //

        // input text and add list
        val inputText : EditText = v.findViewById(R.id.inputText)
        val goBtn  : Button      = v.findViewById(R.id.go_button)

        return v
    }
}