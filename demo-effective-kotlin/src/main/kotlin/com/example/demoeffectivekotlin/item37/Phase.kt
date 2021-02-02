package com.example.demoeffectivekotlin.item37

import java.util.Arrays
import java.util.EnumMap
import java.util.stream.Collectors


enum class Phase {
    SOLID, LIQUID, GAS, PLASMA;
}

enum class Transition(
    val from: Phase, val to: Phase
) {
    MELT(Phase.SOLID, Phase.LIQUID),
    FREEZE(Phase.LIQUID, Phase.SOLID),
    BOIL(Phase.LIQUID, Phase.GAS),
    CONDENSE(Phase.GAS, Phase.LIQUID),
    SUBLIME(Phase.SOLID, Phase.GAS),
    DEPOSIT(Phase.GAS, Phase.SOLID),
    IONIZE(Phase.GAS, Phase.PLASMA),
    DEIONIZE(Phase.PLASMA, Phase.GAS);

    companion object {
        private val m: EnumMap<Phase, MutableMap<Phase, Transition>> = Arrays.stream(values())
            .collect(
                Collectors.groupingBy(
                    { t : Transition -> t.from },
                    { EnumMap(Phase::class.java) },
                    Collectors.toMap(
                        { t -> t.to },
                        { t -> t },
                    )
                )
            )

        fun from(from: Phase, to: Phase): Transition? {
            return m[from]?.get(to)
        }
    }
}

fun main() {
    for (src in Phase.values()) {
        for (dst in Phase.values()) {
            val transition = Transition.from(src, dst)
            if (transition != null) System.out.printf("%s에서 %s로 : %s %n", src, dst, transition)
        }
    }
}
