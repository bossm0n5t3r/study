package com.example.demospringdatajpa.domain.article.type

import org.springframework.data.jpa.repository.JpaRepository

interface ArticleTypeRepository : JpaRepository<ArticleType, Int> {
    fun findByName(articleTypeName: String): ArticleType?
}
