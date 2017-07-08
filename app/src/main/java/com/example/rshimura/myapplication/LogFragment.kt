package com.example.rshimura.myapplication

import android.os.Bundle
import android.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by rshimura on 2017/07/08.
 */
public class LogFragment : Fragment() {
    companion object {
        fun getInstance(): Fragment {
            return LogFragment() as Fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.log_fragment, container, false)
    }
}