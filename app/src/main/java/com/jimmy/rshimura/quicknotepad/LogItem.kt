package com.jimmy.rshimura.quicknotepad

/**
 * Created by rshimura on 12/9/17.
 */
class LogItem (var mode: String, var log: String) {

    //↓getter
    fun getActionNum(): String {
        return mode
    }

    fun getLogText(): String {
        return log
    }
}
