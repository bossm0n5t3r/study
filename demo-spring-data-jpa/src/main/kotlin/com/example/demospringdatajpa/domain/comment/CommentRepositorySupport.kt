package com.example.demospringdatajpa.domain.comment

import com.example.demospringdatajpa.domain.member.Member
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class CommentRepositorySupport(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Comment::class.java) {
    private val comment = QComment.comment

    fun findAll(): List<Comment> {
        return jpaQueryFactory.selectFrom(comment)
            .fetch()
    }

    fun findByMember(member: Member): List<Comment> {
        return jpaQueryFactory.selectFrom(comment)
            .where(comment.member.eq(member))
            .fetch()
    }
}
