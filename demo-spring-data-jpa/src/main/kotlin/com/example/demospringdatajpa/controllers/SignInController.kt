package com.example.demospringdatajpa.controllers

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SignInController {
    @GetMapping("/SignIn")
    fun signIn(model: Model): String {
        return "sign-in"
    }
}
