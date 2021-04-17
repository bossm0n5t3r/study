package com.example.demospringdatajpa.domain.board.type

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class BoardTypeRepositorySupportTest {
    @Autowired
    lateinit var boardTypeRepository: BoardTypeRepository

    @Autowired
    lateinit var boardTypeRepositorySupport: BoardTypeRepositorySupport

    @Test
    fun findAll() {
        // given
        val boardTypeName = "공지"

        boardTypeRepository.save(
            BoardType(name = boardTypeName)
        )

        // when
        val boardTypeList: List<BoardType> = boardTypeRepositorySupport.findAll()

        // when
        assertThat(boardTypeList).isNotEmpty
        assertThat(boardTypeList.size).isEqualTo(1)
        assertThat(boardTypeList.first().name).isEqualTo(boardTypeName)
    }

    @Test
    fun findByName() {
        // given
        val boardTypeName = "공지"

        boardTypeRepository.save(
            BoardType(name = boardTypeName)
        )

        // when
        val boardTypeList: BoardType? = boardTypeRepositorySupport.findByName(boardTypeName)

        // when
        assertThat(boardTypeList).isNotNull
        assertThat(boardTypeList!!.name).isEqualTo(boardTypeName)
    }

    @AfterEach
    fun cleanUp() {
        boardTypeRepository.deleteAll()
    }
}
