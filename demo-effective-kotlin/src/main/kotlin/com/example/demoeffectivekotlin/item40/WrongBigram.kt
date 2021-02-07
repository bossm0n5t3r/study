package com.example.demoeffectivekotlin.item40

import java.util.HashSet

class WrongBigram(
    private val first: Char,
    private val second: Char
) {
    fun equals(b: WrongBigram): Boolean {
        return b.first == first && b.second == second
    }

    fun hashCode(b: WrongBigram): Int {
        return 31 * first.toInt() + second.toInt()
    }
}

fun main() {
    val s: MutableSet<WrongBigram> = HashSet()
    for (i in 0..9) {
        var ch = 'a'
        while (ch <= 'z') {
            s.add(WrongBigram(ch, ch))
            ch++
        }
    }
    println(s.size)
}
