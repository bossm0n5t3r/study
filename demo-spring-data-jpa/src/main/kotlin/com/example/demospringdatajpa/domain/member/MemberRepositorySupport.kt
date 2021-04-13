package com.example.demospringdatajpa.domain.member

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class MemberRepositorySupport(
    private val jpaQueryFactory: JPAQueryFactory
) : QuerydslRepositorySupport(Member::class.java) {
    fun findAll(): List<Member> {
        return jpaQueryFactory.selectFrom(QMember.member)
            .fetch()
    }

    fun findByEmail(email: String): Member? {
        return jpaQueryFactory.selectFrom(QMember.member)
            .where(QMember.member.email.eq(email))
            .fetchOne()
    }

    fun findByName(name: String): Member? {
        return jpaQueryFactory.selectFrom(QMember.member)
            .where(QMember.member.name.eq(name))
            .fetchOne()
    }
}
