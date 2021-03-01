package com.example.demoeffectivekotlin.item46

import java.io.File
import java.util.*
import java.util.Comparator.comparing
import java.util.stream.Collectors.*
import kotlin.streams.toList

fun main() {
    val path = System.getProperty("user.dir")
    val wordFilePath = "$path/src/main/kotlin/com/example/demoeffectivekotlin/item46/words.txt"
//    doesNotUnderstandStreamParadigm(wordFilePath = wordFilePath)
    val freq = wellUnderstandStreamParadigm(wordFilePath = wordFilePath)
    println(freq)
    println()
    val topTen = freq?.let { topTen(it) }
    println(topTen)
    println()
}

private fun doesNotUnderstandStreamParadigm(wordFilePath: String) {
    val wordFile = File(wordFilePath)
    val freq = mutableMapOf<String, Long>() as HashMap
    try {
        val words = Scanner(wordFile).tokens()
        words.forEach { word ->
            freq.merge(word.toLowerCase(), 1L, Long::plus)
        }
        freq.toList()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    println(freq)
}

private fun wellUnderstandStreamParadigm(wordFilePath: String): Map<String, Long>? {
    val wordFile = File(wordFilePath)
    var freq : Map<String, Long>? = null
    try {
        val words = Scanner(wordFile).tokens()
        freq = words
                .collect(groupingBy(String::toLowerCase, counting()))
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return freq
}

private fun topTen(freq: Map<String, Long>): List<String> {
    return freq.keys.stream()
            .sorted(comparing(freq::getValue).reversed())
            .limit(10)
            .toList()
}
