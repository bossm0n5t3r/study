package com.example.demospringdatajpa.domain.member

import com.example.demospringdatajpa.domain.BaseEntity
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member_info")
class Member(
    val email: String,
    val password: String,
    val name: String
) : BaseEntity<Int>()
