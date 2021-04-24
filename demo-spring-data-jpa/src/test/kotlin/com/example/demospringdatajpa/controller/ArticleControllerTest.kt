package com.example.demospringdatajpa.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest
class ArticleControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `index 가 리턴된다`() {
        val index = "index"

        mvc.perform(
            MockMvcRequestBuilders.get("/")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name(index))
    }

    @Test
    fun `write-article 이 리턴된다`() {
        val writeArticle = "write-article"

        mvc.perform(
            MockMvcRequestBuilders.get("/write")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name(writeArticle))
    }

    @Test
    fun `edit-article 이 리턴된다`() {
        val editArticle = "edit-article"

        mvc.perform(
            MockMvcRequestBuilders.get("/edit")
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.view().name(editArticle))
    }
}
