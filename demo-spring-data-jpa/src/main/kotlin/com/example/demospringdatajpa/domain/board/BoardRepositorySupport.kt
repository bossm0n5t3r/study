package com.example.demospringdatajpa.domain.board

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class BoardRepositorySupport(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Board::class.java) {
    private val board = QBoard.board

    fun findAll(): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .fetch()
    }

    fun findById(boardId: Int): Board? {
        return jpaQueryFactory.selectFrom(board)
            .where(board.id.eq(boardId))
            .fetchOne()
    }

    fun updateSubject(boardId: Int, newSubject: String) {
        jpaQueryFactory.update(board)
            .where(board.id.eq(boardId))
            .set(board.subject, newSubject)
            .execute()
    }
}
