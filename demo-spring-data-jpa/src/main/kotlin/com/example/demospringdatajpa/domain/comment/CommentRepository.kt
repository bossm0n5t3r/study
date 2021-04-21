package com.example.demospringdatajpa.domain.comment

import com.example.demospringdatajpa.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Int> {
    fun findAllByMember(member: Member): List<Comment>
}
