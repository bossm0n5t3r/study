package com.example.demoeffectivekotlin.item45.anagrams

import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.Arrays
import java.util.Scanner
import java.util.TreeSet

class IterativeAnagrams(
    private val dictionary: File,
    private val minGroupSize: Int
) {
    fun run() {
        val groups = mutableMapOf<String, TreeSet<String>>()
        try {
            val s = Scanner(dictionary)
            while (s.hasNext()) {
                val word = s.next()
                groups.computeIfAbsent(
                    alphabetize(word)
                ) { TreeSet<String>() }.add(word)
            }
        } catch (e: IOException) { }

        for (group in groups.values) {
            if (group.size >= minGroupSize) {
                println(group.size.toString() + ": " + group)
            }
        }
    }

    private fun alphabetize(s: String): String {
        val a = s.toCharArray()
        Arrays.sort(a)
        return String(a)
    }
}

fun main() {
    println("Example : IterativeAnagrams")
    val path = Paths.get("").toAbsolutePath().toString()
    val dictionaryFilePath = "$path/src/main/kotlin/com/example/demoeffectivekotlin/item45/anagrams/dictionary.txt"
    val dictionary = File(dictionaryFilePath)
    val minGroupSize = 1

    val iterativeAnagrams = IterativeAnagrams(dictionary, minGroupSize)
    iterativeAnagrams.run()
}
