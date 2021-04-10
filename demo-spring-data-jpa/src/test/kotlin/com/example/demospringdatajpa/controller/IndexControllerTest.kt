package com.example.demospringdatajpa.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest
class IndexControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `index가 리턴된다`() {
        val index = "index"

        mvc.perform(
            get("/")
        )
            .andExpect(status().isOk)
            .andExpect(view().name(index))
    }
}
