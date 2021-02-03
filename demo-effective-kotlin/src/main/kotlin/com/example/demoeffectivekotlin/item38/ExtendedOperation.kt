package com.example.demoeffectivekotlin.item38

import kotlin.math.pow

enum class ExtendedOperation(private val symbol: String) : Operation {
    EXP("^") {
        override fun apply(x: Double, y: Double): Double {
            return x.pow(y)
        }
    },
    REMAINDER("%") {
        override fun apply(x: Double, y: Double): Double {
            return x % y
        }
    };

    override fun toString(): String {
        return symbol
    }
}

fun main() {
    val x = 2.0
    val y = 4.0
    test(ExtendedOperation.values().toList(), x, y)
}

private fun <T : Operation> test(
    opSet: Collection<T>,
    x: Double,
    y: Double
) {
    for (op in opSet)
        System.out.printf(
            "%f %s %f = %f%n",
            x, op, y, op.apply(x, y)
        )
}
