package com.example.demospringdatajpa.domain.article.type

import com.example.demospringdatajpa.domain.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "article_type_info")
class ArticleType(
    @Column(name = "name", nullable = false, updatable = true)
    var name: String
) : BaseEntity<Int>()
