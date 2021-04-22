package com.example.demospringdatajpa.domain.comment

import com.example.demospringdatajpa.domain.BaseEntity
import com.example.demospringdatajpa.domain.article.Article
import com.example.demospringdatajpa.domain.member.Member
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "comment_info")
class Comment(
    content: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false, updatable = false)
    var article: Article,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    var member: Member
) : BaseEntity<Int>() {
    @Column(name = "content", nullable = false, updatable = true)
    var content: String = content

    fun updateComment(updateCommentDto: UpdateCommentDto) {
        this.content = updateCommentDto.content
    }
}

data class UpdateCommentDto(
    val content: String
)
