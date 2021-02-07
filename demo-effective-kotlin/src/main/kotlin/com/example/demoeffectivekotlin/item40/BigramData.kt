package com.example.demoeffectivekotlin.item40

import java.util.HashSet

data class BigramData(
    private val first: Char,
    private val second: Char
)

fun main() {
    val s: MutableSet<BigramData> = HashSet()
    for (i in 0..9) {
        var ch = 'a'
        while (ch <= 'z') {
            s.add(BigramData(ch, ch))
            ch++
        }
    }
    println(s.size)
}
