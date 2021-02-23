package com.example.demoeffectivekotlin.item45.anagrams

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Arrays
import java.util.stream.Collectors.groupingBy
import java.util.stream.Stream

class HybridAnagrams(
    private val dictionary: Path,
    private val minGroupSize: Int
) {
    fun run() {
        try {
            val words: Stream<String> = Files.lines(dictionary)
            words.collect(
                groupingBy {
                    word: String ->
                    alphabetize(word)
                }
            )
                .values.stream()
                .filter { group: List<String> -> group.size >= minGroupSize }
                .forEach { group: List<String> -> println(group.size.toString() + ": " + group) }
        } catch (e: IOException) {}
    }

    private fun alphabetize(s: String): String {
        val a = s.toCharArray()
        Arrays.sort(a)
        return String(a)
    }
}

fun main() {
    println("Example : HybridAnagrams")
    val path = System.getProperty("user.dir")
    val dictionaryFilePath = "$path/src/main/kotlin/com/example/demoeffectivekotlin/item45/anagrams/dictionary.txt"
    val dictionary: Path = Paths.get(dictionaryFilePath)
    val minGroupSize = 1

    val hybridAnagrams = HybridAnagrams(dictionary, minGroupSize)
    hybridAnagrams.run()
}
