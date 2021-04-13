package com.example.demospringdatajpa.domain.board.type

import com.example.demospringdatajpa.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "board_type_info")
class BoardType(
    @Column(name = "name", nullable = false, updatable = true)
    var name: String
) : BaseEntity<Int>()
