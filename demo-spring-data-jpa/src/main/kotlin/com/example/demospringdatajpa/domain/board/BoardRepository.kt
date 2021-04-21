package com.example.demospringdatajpa.domain.board

import com.example.demospringdatajpa.domain.board.type.BoardType
import com.example.demospringdatajpa.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository : JpaRepository<Board, Int> {
    fun findByMember(member: Member): List<Board>
    fun findByBoardType(boardType: BoardType): List<Board>
    fun findAllByOrderByHitsAsc(): List<Board>
    fun findAllByOrderByHitsDesc(): List<Board>
    fun findAllByOrderByRecommendAsc(): List<Board>
    fun findAllByOrderByRecommendDesc(): List<Board>
}
