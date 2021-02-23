package com.example.demoeffectivekotlin.item45

import com.example.demoeffectivekotlin.item45.Card.Companion.NEW_DECK_LOOP
import com.example.demoeffectivekotlin.item45.Card.Companion.NEW_DECK_STREAM
import java.util.ArrayList

class Card(
    private val suit: Suit,
    private val rank: Rank
) {
    enum class Suit {
        SPADE, HEART, DIAMOND, CLUB
    }

    enum class Rank {
        ACE, DEUCE, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }

    override fun toString(): String {
        return rank.toString() + " of " + suit + "S"
    }

    companion object {
        val NEW_DECK_LOOP = newDeckLoop()
        val NEW_DECK_STREAM = newDeckStream()

        // 데카르트 곱 계산을 반복 방식으로 구현
        private fun newDeckLoop(): List<Card> {
            val result: MutableList<Card> = ArrayList()
            for (suit in Suit.values()) for (rank in Rank.values()) result.add(Card(suit, rank))
            return result
        }

        // 데카르트 곱 계산을 스트림 방식으로 구현
        private fun newDeckStream(): List<Card> {
            return Suit.values()
                .flatMap { suit -> Rank.values().map { rank -> Card(suit, rank) } }
                .toList()
        }
    }
}

fun main() {
    println(NEW_DECK_LOOP)
    println(NEW_DECK_STREAM)
}
