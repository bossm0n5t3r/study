package com.example.demoeffectivekotlin.item45

fun main() {
    "Hello, world!".chars().forEach(System.out::print) // 72101108108111443211911111410810033
    println()
    "Hello, world!".chars().forEach { x: Int -> print(x.toChar()) } // Hello, world!
}
