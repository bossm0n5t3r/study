package com.example.demospringdatajpa.domain.comment

import org.springframework.data.jpa.repository.JpaRepository

interface CommentRepository : JpaRepository<Comment, Int>
