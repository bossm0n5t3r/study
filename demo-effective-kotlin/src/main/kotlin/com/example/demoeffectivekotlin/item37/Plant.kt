package com.example.demoeffectivekotlin.item37

import java.util.Arrays
import java.util.EnumMap
import java.util.stream.Collectors

// EnumMap을 사용해 열거 타입에 데이터를 연관시키기 (226-228쪽)
// 식물을 아주 단순하게 표현한 클래스 (226쪽)
data class Plant(
    val name: String,
    val lifeCycle: LifeCycle
) {
    enum class LifeCycle {
        ANNUAL, PERENNIAL, BIENNIAL
    }
}

fun main() {
    val garden = arrayOf(
        Plant("바질", Plant.LifeCycle.ANNUAL),
        Plant("캐러웨이", Plant.LifeCycle.BIENNIAL),
        Plant("딜", Plant.LifeCycle.ANNUAL),
        Plant("라벤더", Plant.LifeCycle.PERENNIAL),
        Plant("파슬리", Plant.LifeCycle.BIENNIAL),
        Plant("로즈마리", Plant.LifeCycle.PERENNIAL)
    )

    // 코드 37-1 ordinal()을 배열 인덱스로 사용 - 따라 하지 말 것! (226쪽)
    val plantsByLifeCycleArr = arrayOfNulls<MutableSet<*>>(Plant.LifeCycle.values().size) as Array<MutableSet<Plant>>
    for (i in plantsByLifeCycleArr.indices) plantsByLifeCycleArr[i] = HashSet()
    for (p in garden) plantsByLifeCycleArr[p.lifeCycle.ordinal].add(p)
    // 결과 출력
    println("코드 37-1 ordinal()을 배열 인덱스로 사용 - 따라 하지 말 것!")
    for (i in plantsByLifeCycleArr.indices) {
        System.out.printf(
            "%s: %s%n",
            Plant.LifeCycle.values()[i], plantsByLifeCycleArr[i]
        )
    }
    println("\n")

    // 코드 37-2 EnumMap을 사용해 데이터와 열거 타입을 매핑한다. (227쪽)
    val plantsByLifeCycle: MutableMap<Plant.LifeCycle, MutableSet<Plant>> = EnumMap(Plant.LifeCycle::class.java)
    for (lc in Plant.LifeCycle.values()) plantsByLifeCycle[lc] = HashSet()
    for (p in garden) plantsByLifeCycle[p.lifeCycle]!!.add(p)
    println("코드 37-2 EnumMap을 사용해 데이터와 열거 타입을 매핑한다.")
    println(plantsByLifeCycle)
    println("\n")

    // 코드 37-3 스트림을 사용한 코드 1 - EnumMap을 사용하지 않는다! (228쪽)
    println(
        "코드 37-3 스트림을 사용한 코드 1 - EnumMap을 사용하지 않는다!"
    )
    println(
        Arrays.stream(garden)
            .collect(Collectors.groupingBy { p: Plant -> p.lifeCycle })
    )
    println("\n")

    // 코드 37-4 스트림을 사용한 코드 2 - EnumMap을 이용해 데이터와 열거 타입을 매핑했다. (228쪽)
    println("코드 37-4 스트림을 사용한 코드 2 - EnumMap을 이용해 데이터와 열거 타입을 매핑했다.")
    println(
        Arrays.stream(garden)
            .collect(
                Collectors.groupingBy(
                    { p: Plant -> p.lifeCycle },
                    { EnumMap(Plant.LifeCycle::class.java) }, Collectors.toSet()
                )
            )
    )
    println("\n")
}
