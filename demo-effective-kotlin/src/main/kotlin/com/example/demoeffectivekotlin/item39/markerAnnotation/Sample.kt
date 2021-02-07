package com.example.demoeffectivekotlin.item39.markerAnnotation

import java.lang.RuntimeException
import java.lang.reflect.InvocationTargetException

class Sample {
    companion object {
        @JvmStatic @Test fun m1() {}
        @JvmStatic fun m2() {}
        @JvmStatic @Test fun m3() { throw RuntimeException("m3은 실패") }
        @JvmStatic fun m4() {}
        @JvmStatic fun m6() {}
        @JvmStatic @Test fun m7() { throw RuntimeException("m7은 실패") }
        @JvmStatic fun m8() {}
    }
    @Test fun m5() {}
}

fun main() {
    var tests = 0
    var passed = 0
    val testClass: Class<*> = Sample::class.java
    for (m in testClass.declaredMethods) {
        if (m.isAnnotationPresent(Test::class.java)) {
            tests++
            try {
                m.invoke(null)
                passed++
            } catch (wrappedExc: InvocationTargetException) {
                val exc = wrappedExc.cause
                println("$m 실패: $exc")
            } catch (exc: Exception) {
                println("잘못 사용한 @Test: $m")
            }
        }
    }
    System.out.printf(
        "성공: %d, 실패: %d%n",
        passed, tests - passed
    )
}
