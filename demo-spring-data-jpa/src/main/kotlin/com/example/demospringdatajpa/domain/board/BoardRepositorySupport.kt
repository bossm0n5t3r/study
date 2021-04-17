package com.example.demospringdatajpa.domain.board

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositorySupport(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Board::class.java) {
    fun findAll(): List<Board> {
        return jpaQueryFactory.selectFrom(QBoard.board)
            .fetch()
    }
}
