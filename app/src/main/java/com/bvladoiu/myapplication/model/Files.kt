package com.bvladoiu.myapplication.model

import java.util.Collections.min

object Files {

    private lateinit var items: MutableList<Int>

    init {
        loadFiles()
    }

    fun loadFiles() {
        items = (1..4).toMutableList()
    }

    fun archive(vararg args: Int) {
        var min = args.minOrNull()
    }

    fun getFiles(): List<Int> = items

    fun addAll(list: Collection<Int>) =
        items.addAll(list)

    fun size(): Int = items.size
    fun removeAt(i: Int)  = items.removeAt(i)

}