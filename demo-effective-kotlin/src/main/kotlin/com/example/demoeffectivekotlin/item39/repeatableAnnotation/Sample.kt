package com.example.demoeffectivekotlin.item39.repeatableAnnotation

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

        @JvmStatic
        @ExceptionTestContainer(
            ExceptionTest(IndexOutOfBoundsException::class),
            ExceptionTest(NullPointerException::class)
        )
        fun doublyBad() {
            val n: String? = null
            println(n!!)
        }
    }
}

fun main() {
    var tests = 0
    var passed = 0
    val testClass = Sample::class.java
    for (m in testClass.declaredMethods) {
        if (
            m.isAnnotationPresent(ExceptionTest::class.java) ||
            m.isAnnotationPresent(ExceptionTestContainer::class.java)
        ) {
            tests++
            try {
                m.invoke(null)
                System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m)
            } catch (wrappedEx: Throwable) {
                val exc = wrappedEx.cause
                val oldPassed = passed
                val excTests: Array<ExceptionTest> = m.getAnnotationsByType(ExceptionTest::class.java)
                for (excTest in excTests) {
                    if (excTest.value.isInstance(exc)) {
                        passed++
                        break
                    }
                }
                if (passed == oldPassed) {
                    System.out.printf(
                        "테스트 %s 실패: %s %n",
                        m, exc
                    )
                }
            }
        }
    }

    System.out.printf(
        "성공: %d, 실패: %d%n",
        passed, tests - passed
    )
}
