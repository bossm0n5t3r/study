package com.example.demoeffectivekotlin.item39.annotationWithParameter

import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import kotlin.reflect.KClass

class Sample {
    companion object {
        @JvmStatic
        @ExceptionTest(ArithmeticException::class)
        fun m1() {
            var i = 0
            i /= i
        }

        @JvmStatic
        @ExceptionTest(ArithmeticException::class)
        fun m2() {
            val a = IntArray(0)
            val i = a[1]
        }

        @JvmStatic
        @ExceptionTest(ArithmeticException::class)
        fun m3() {}
    }
}

fun main() {
    var tests = 0
    var passed = 0
    val testClass = Sample::class.java
    for (m in testClass.declaredMethods) {
        if (m.isAnnotationPresent(ExceptionTest::class.java)) {
            tests++
            try {
                m.invoke(null)
                System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m)
            } catch (wrappedEx: InvocationTargetException) {
                val exc = wrappedEx.cause
                val excType: KClass<out Throwable> = m.getAnnotation(ExceptionTest::class.java).value
                if (excType.isInstance(exc)) {
                    passed++
                } else {
                    System.out.printf(
                        "테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n",
                        m, excType.simpleName, exc
                    )
                }
            } catch (exc: Exception) {
                println("잘못 사용한 @ExceptionTest: $m")
            }
        }
    }

    System.out.printf(
        "성공: %d, 실패: %d%n",
        passed, tests - passed
    )
}
