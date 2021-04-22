package com.example.demospringdatajpa.domain.article

import com.example.demospringdatajpa.domain.BaseEntity
import com.example.demospringdatajpa.domain.article.type.ArticleType
import com.example.demospringdatajpa.domain.member.Member
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "article_info")
class Article(
    subject: String,

    content: String,

    @Column(name = "hits", nullable = false, updatable = true)
    var hits: Int? = 0,

    @Column(name = "recommend", nullable = false)
    var recommend: Int? = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, updatable = false)
    var member: Member,

    articleType: ArticleType
) : BaseEntity<Int>() {
    @Column(name = "subject", nullable = false)
    var subject: String = subject

    @Column(name = "content", nullable = false)
    var content: String = content

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_type_id", nullable = false, updatable = true)
    var articleType: ArticleType = articleType

    fun updateArticle(updateArticleDto: UpdateArticleDto) {
        updateArticleDto.subject?.let { this.subject = it }
        updateArticleDto.content?.let { this.content = it }
        updateArticleDto.articleType?.let { this.articleType = it }
    }
}

data class UpdateArticleDto(
    val subject: String? = null,
    val content: String? = null,
    val articleType: ArticleType? = null
)
