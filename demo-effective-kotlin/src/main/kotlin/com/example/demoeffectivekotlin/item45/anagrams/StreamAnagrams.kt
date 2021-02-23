package com.example.demoeffectivekotlin.item45.anagrams

import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Collectors.groupingBy
import java.util.stream.Stream

class StreamAnagrams(
    private val dictionary: Path,
    private val minGroupSize: Int
) {
    fun run() {
        try {
            val words: Stream<String> = Files.lines(dictionary)
            words.collect(
                groupingBy { word: String ->
                    word.chars().sorted()
                        .collect(
                            { StringBuilder() },
                            { sb: StringBuilder, c: Int -> sb.append(c.toChar()) },
                            StringBuilder::append
                        ).toString()
                }
            )
                .values.stream()
                .filter { group: List<String> -> group.size >= minGroupSize }
                .map { group: List<String> -> group.size.toString() + ": " + group }
                .forEach(System.out::println)
        } catch (e: IOException) {}
    }
}

fun main() {
    println("Example : StreamAnagrams")
    val path = System.getProperty("user.dir")
    val dictionaryFilePath = "$path/src/main/kotlin/com/example/demoeffectivekotlin/item45/anagrams/dictionary.txt"
    val dictionary: Path = Paths.get(dictionaryFilePath)
    val minGroupSize = 1

    val streamAnagrams = StreamAnagrams(dictionary, minGroupSize)
    streamAnagrams.run()
}
