package com.example.demospringdatajpa.domain.article

import com.example.demospringdatajpa.domain.article.type.ArticleType
import com.example.demospringdatajpa.domain.member.Member
import org.springframework.data.jpa.repository.JpaRepository

interface ArticleRepository : JpaRepository<Article, Int> {
    fun findByMember(member: Member): List<Article>
    fun findByArticleType(articleType: ArticleType): List<Article>
    fun findAllByOrderByHitsAsc(): List<Article>
    fun findAllByOrderByHitsDesc(): List<Article>
    fun findAllByOrderByRecommendAsc(): List<Article>
    fun findAllByOrderByRecommendDesc(): List<Article>
}
