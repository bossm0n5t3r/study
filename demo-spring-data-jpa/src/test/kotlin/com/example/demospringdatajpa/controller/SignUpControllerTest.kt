package com.example.demospringdatajpa.controller

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest
class SignUpControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `sign-up이 리턴된다`() {
        val signUp = "sign-up"

        mvc.perform(
            get("/SignUp")
        )
            .andExpect(status().isOk)
            .andExpect(view().name(signUp))
    }
}