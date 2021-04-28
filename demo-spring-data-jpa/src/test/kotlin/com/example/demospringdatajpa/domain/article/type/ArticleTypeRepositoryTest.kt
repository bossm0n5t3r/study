package com.example.demospringdatajpa.domain.article.type

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class ArticleTypeRepositoryTest {
    @Autowired
    lateinit var articleTypeRepository: ArticleTypeRepository

    @Test
    fun findAll() {
        // given
        val articleTypeName = "공지"

        articleTypeRepository.save(
            ArticleType(name = articleTypeName)
        )

        // when
        val articleTypeList = articleTypeRepository.findAll()

        // then
        assertThat(articleTypeList).isNotEmpty
        assertThat(articleTypeList.size).isEqualTo(1)
        assertThat(articleTypeList[0].name).isEqualTo(articleTypeName)
    }

    @Test
    fun findByName() {
        // given
        val articleTypeName = "공지"

        articleTypeRepository.save(
            ArticleType(name = articleTypeName)
        )

        // when
        val articleType = articleTypeRepository.findByName(articleTypeName)

        // then
        assertThat(articleType).isNotNull
        assertThat(articleType!!.name).isEqualTo(articleTypeName)
    }

    @AfterEach
    fun cleanUp() {
        articleTypeRepository.deleteAll()
    }
}
