package com.example.demospringdatajpa.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SignInController {
    @GetMapping("/SignIn")
    fun signInPage(model: Model): String {
        return "sign-in"
    }
}
