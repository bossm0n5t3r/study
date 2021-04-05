package com.example.demospringdatajpa.controllers

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.view

@WebMvcTest
class SignInControllerTest {
    @Autowired
    lateinit var mvc: MockMvc

    @Test
    fun `sign-in이 리턴된다`() {
        val signIn = "sign-in"

        mvc.perform(
            get("/SignIn")
        )
            .andExpect(status().isOk)
            .andExpect(view().name(signIn))
    }
}
