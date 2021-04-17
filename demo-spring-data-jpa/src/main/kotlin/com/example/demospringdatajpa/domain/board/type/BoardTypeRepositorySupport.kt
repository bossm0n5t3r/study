package com.example.demospringdatajpa.domain.board.type

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardTypeRepositorySupport(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(BoardType::class.java) {
    fun findAll(): List<BoardType> {
        return jpaQueryFactory.selectFrom(QBoardType.boardType)
            .fetch()
    }

    fun findByName(boardTypeName: String): BoardType? {
        return jpaQueryFactory.selectFrom(QBoardType.boardType)
            .where(QBoardType.boardType.name.eq(boardTypeName))
            .fetchOne()
    }
}
