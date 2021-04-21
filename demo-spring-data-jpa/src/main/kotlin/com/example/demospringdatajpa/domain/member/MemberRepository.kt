package com.example.demospringdatajpa.domain.member

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface MemberRepository : JpaRepository<Member, Int>, QuerydslPredicateExecutor<Member> {
    fun findByEmail(email: String): Member?
    fun findAllByName(name: String): List<Member>
}
