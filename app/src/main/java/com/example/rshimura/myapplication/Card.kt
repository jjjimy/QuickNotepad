package com.example.rshimura.myapplication

/**
 * Created by rshimura on 2017/07/09.
 */
public class Card(var todo: String, var date: String) {

    //↓getter
    fun getTodoStr(): String {
        return todo
    }

    fun getDateStr(): String {
        return date
    }
}


