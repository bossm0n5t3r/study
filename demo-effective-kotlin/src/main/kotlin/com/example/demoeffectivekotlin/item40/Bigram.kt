package com.example.demoeffectivekotlin.item40

import java.util.HashSet

class Bigram(
    val first: Char,
    val second: Char
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Bigram) {
            return false
        }
        return other.first == first && other.second == second
    }

    override fun hashCode(): Int {
        return 31 * first.toInt() + second.toInt()
    }
}

fun main() {
    val s: MutableSet<Bigram> = HashSet()
    for (i in 0..9) {
        var ch = 'a'
        while (ch <= 'z') {
            s.add(Bigram(ch, ch))
            ch++
        }
    }
    println(s.size)
}
