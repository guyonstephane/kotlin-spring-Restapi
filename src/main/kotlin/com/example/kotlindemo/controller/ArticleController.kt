package com.example.kotlindemo.controller


import org.springframework.ui.Model
import org.springframework.ui.set
import com.example.kotlindemo.model.Article
import com.example.kotlindemo.repository.ArticleRepository
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import java.util.*


/**
 * Created by rajeevkumarsingh on 04/10/17.
 */

@Controller
class ArticleController @Autowired constructor(private val articleRepository: ArticleRepository){



    @GetMapping("/article")
    fun index(model: Model): String {
        model["title"] = "Les articles"
        model["articles"] = articleRepository.findAll()
        println(model)
        return "article/index"
    }


    @PostMapping("/articles")
    fun createNewArticle(@Valid @RequestBody article: Article): Article =
        articleRepository.save(article)


    @GetMapping("/articles/{id}")
    fun getArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Article> {
        return articleRepository.findById(articleId).map { article ->
            ResponseEntity.ok(article)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PutMapping("/articles/{id}")
    fun updateArticleById(@PathVariable(value = "id") articleId: Long,
                          @Valid @RequestBody newArticle: Article): ResponseEntity<Article> {

        return articleRepository.findById(articleId).map { existingArticle ->
            val updatedArticle: Article = existingArticle
                .copy(title = newArticle.title, content = newArticle.content)

            ResponseEntity.ok().body(articleRepository.save(updatedArticle))
        }.orElse(ResponseEntity.notFound().build())

    }

    @DeleteMapping("/articles/{id}")
    fun deleteArticleById(@PathVariable(value = "id") articleId: Long): ResponseEntity<Void> {

        return articleRepository.findById(articleId).map { article  ->
            articleRepository.delete(article)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }
}