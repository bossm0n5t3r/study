package com.example.demoeffectivekotlin.item43

import java.util.TreeMap

fun main() {
    val frequencyTable = TreeMap<String, Int>()
    val args = arrayOf("a", "b", "c", "d", "e", "a")

    println("Result : Lambda")
    // Lambda
    for (s in args) frequencyTable.merge(s, 1) { count: Int, incr: Int -> count + incr }
    println(frequencyTable)
    println()

    // clear
    frequencyTable.clear()

    println("Result : Integer::sum")
    // Integer::sum
    for (s in args) frequencyTable.merge(s, 1, Integer::sum)
    println(frequencyTable)
    println()

    // clear
    frequencyTable.clear()
}
