package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rshimura on 2017/07/08.
 */
public class LookFragment : Fragment() {
    companion object {
        fun getInstance(): Fragment {
            return LookFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.look_fragment, container, false)
    }
}