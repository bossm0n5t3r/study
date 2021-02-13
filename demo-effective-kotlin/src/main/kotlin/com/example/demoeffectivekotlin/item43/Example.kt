package com.example.demoeffectivekotlin.item43

import java.time.Instant
import java.util.TreeMap


fun main() {
    // 정적 메서드 참조 유형
    val referenceToAStaticMethod = { str: String -> Integer.parseInt(str) }

    // 한정적 (인스턴스) 메서드 참조 유형
    val then = Instant.now()
    val referenceToAnInstanceMethodOfAParticularObject = { t: Instant -> then.isAfter(t) }

    // 비한정적 (인스턴스) 메서드 참조 유형
    val referenceToAnInstanceMethodOfAnArbitraryObjectOfAParticularType = { str: String -> str.toLowerCase() }

    // 클래스 생성자를 가리키는 메서드 참조 유형
    val referenceToAConstructor = { TreeMap<String, Int>() }

    // 배열 생성자를 가리키는 메서드 참조 유형
    val referenceToAArrayConstructor = { len: Int -> IntArray(len) }
}
