package com.example.demospringdatajpa.domain.board

import com.example.demospringdatajpa.domain.board.type.BoardType
import com.example.demospringdatajpa.domain.member.Member
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
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

    fun findByMember(member: Member): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .where(board.member.eq(member))
            .fetch()
    }

    fun findByBoardType(boardType: BoardType): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .where(board.boardType.eq(boardType))
            .fetch()
    }

    fun orderByHits(order: Order): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .orderBy(OrderSpecifier(order, board.hits))
            .fetch()
    }

    fun orderByRecommend(order: Order): List<Board> {
        return jpaQueryFactory.selectFrom(board)
            .orderBy(OrderSpecifier(order, board.recommend))
            .fetch()
    }
}
