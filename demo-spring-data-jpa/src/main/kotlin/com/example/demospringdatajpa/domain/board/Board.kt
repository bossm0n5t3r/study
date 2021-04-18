package com.example.demospringdatajpa.domain.board

import com.example.demospringdatajpa.domain.BaseEntity
import com.example.demospringdatajpa.domain.board.type.BoardType
import com.example.demospringdatajpa.domain.member.Member
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "board_info")
class Board(
    subject: String,

    content: String,

    @Column(name = "hits", nullable = false, updatable = true)
    var hits: Int? = 0,

    @Column(name = "recommend", nullable = false)
    var recommend: Int? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    var member: Member,

    boardType: BoardType
) : BaseEntity<Int>() {
    @Column(name = "subject", nullable = false)
    var subject: String = subject

    @Column(name = "content", nullable = false)
    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_type_id", nullable = false, updatable = true)
    var boardType: BoardType = boardType

    fun updateBoard(updateBoardDto: UpdateBoardDto) {
        updateBoardDto.subject?.let { this.subject = it }
        updateBoardDto.content?.let { this.content = it }
        updateBoardDto.boardType?.let { this.boardType = it }
    }
}

data class UpdateBoardDto(
    val subject: String? = null,
    val content: String? = null,
    val boardType: BoardType? = null
)
