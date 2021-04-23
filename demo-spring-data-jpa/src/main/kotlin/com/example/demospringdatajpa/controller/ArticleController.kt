package com.example.demospringdatajpa.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ArticleController {
    @GetMapping("/write")
    fun write(model: Model): String {
        return "write-article"
    }

    @GetMapping("/edit")
    fun edit(model: Model): String {
        return "edit-article"
    }
}
