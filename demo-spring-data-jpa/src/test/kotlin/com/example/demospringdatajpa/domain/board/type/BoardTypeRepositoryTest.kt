package com.example.demospringdatajpa.domain.board.type

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class BoardTypeRepositoryTest {
    @Autowired
    lateinit var boardTypeRepository: BoardTypeRepository

    @Test
    fun findAll() {
        // given
        val boardTypeName = "공지"

        boardTypeRepository.save(
            BoardType(name = boardTypeName)
        )

        // when
        val boardTypeList = boardTypeRepository.findAll()

        // then
        assertThat(boardTypeList).isNotEmpty
        assertThat(boardTypeList.size).isEqualTo(1)
        assertThat(boardTypeList[0].name).isEqualTo(boardTypeName)
    }

    @Test
    fun findByName() {
        // given
        val boardTypeName = "공지"

        boardTypeRepository.save(
            BoardType(name = boardTypeName)
        )

        // when
        val boardType = boardTypeRepository.findByName(boardTypeName)

        // then
        assertThat(boardType).isNotNull
        assertThat(boardType!!.name).isEqualTo(boardTypeName)
    }

    @AfterEach
    fun cleanUp() {
        boardTypeRepository.deleteAll()
    }
}
