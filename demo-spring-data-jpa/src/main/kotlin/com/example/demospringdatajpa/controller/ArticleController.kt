package com.example.demospringdatajpa.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ArticleController {
    @GetMapping("/")
    fun indexPage(model: Model): String {
        return "index"
    }

    @GetMapping("/write")
    fun writeArticlePage(model: Model): String {
        return "write-article"
    }

    @GetMapping("/edit")
    fun editArticlePage(model: Model): String {
        return "edit-article"
    }
}
