package com.example.demospringdatajpa.domain.comment

import com.example.demospringdatajpa.domain.article.Article
import com.example.demospringdatajpa.domain.article.ArticleRepository
import com.example.demospringdatajpa.domain.article.type.ArticleType
import com.example.demospringdatajpa.domain.article.type.ArticleTypeRepository
import com.example.demospringdatajpa.domain.member.Member
import com.example.demospringdatajpa.domain.member.MemberRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class CommentRepositoryTest {
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var articleTypeRepository: ArticleTypeRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository
    @Autowired
    lateinit var commentRepository: CommentRepository

    private val commentContent = "기본 댓글"

    private fun createMemberAndArticleTypeAndArticle(case: Int = 1): Pair<List<Member>, List<Article>> {
        // member
        val email = "test@test.test"
        val password = "password"
        val name = "name"

        for (i in 1..case) {
            memberRepository.save(
                Member(
                    email = "$i$email",
                    password = "$i$password",
                    name = "$i$name"
                )
            )
        }

        val articleTypeName = "공지"
        articleTypeRepository.save(
            ArticleType(name = articleTypeName)
        )

        val memberList = memberRepository.findAll().toList()
        val member = memberList.first()
        val articleType = articleTypeRepository.findAll().first()

        val subject = "테스트 제목"
        val content = "테스트 본문"

        memberList.forEach {
            val index = it.email[0].toString()
            articleRepository.save(
                Article(
                    subject = "$index$subject",
                    content = "$index$content",
                    member = member,
                    articleType = articleType
                )
            )
        }

        val articleList = articleRepository.findAll()

        return Pair(memberList, articleList)
    }

    @Test
    fun findAll() {
        val (memberList, articleList) = createMemberAndArticleTypeAndArticle()

        val member = memberList.first()
        val article = articleList.first()

        val numberOfComment = 9
        for (i in 1..numberOfComment) {
            commentRepository.save(
                Comment(
                    content = "$commentContent$i",
                    article = article,
                    member = member
                )
            )
        }

        val commentList: List<Comment> = commentRepository.findAll()

        assertThat(commentList).isNotEmpty
        assertThat(commentList.size).isEqualTo(numberOfComment)
    }

    @Test
    fun findByMember() {
        val case = 10
        val (memberList, articleList) = createMemberAndArticleTypeAndArticle(case = case)

        val article = articleList.first()

        memberList.forEach {
            val index = it.email[0].toString()
            commentRepository.save(
                Comment(
                    content = "$index$commentContent",
                    article = article,
                    member = it
                )
            )
        }

        val firstMember = memberList.first()
        val commentList: List<Comment> = commentRepository.findAllByMember(firstMember)

        assertThat(commentList).isNotEmpty
        assertThat(commentList.size).isEqualTo(1)

        val checkingIndex = firstMember.email[0].toString()
        assertThat(commentList[0].content[0].toString()).isEqualTo(checkingIndex)
    }

    @Test
    fun updateCommentContent() {
        val (memberList, articleList) = createMemberAndArticleTypeAndArticle()

        val member = memberList.first()
        val article = articleList.first()

        commentRepository.save(
            Comment(
                content = commentContent,
                article = article,
                member = member
            )
        )

        var commentList = commentRepository.findAll()
        assertThat(commentList).isNotEmpty
        assertThat(commentList.size).isEqualTo(1)
        assertThat(commentList[0].content).isEqualTo(commentContent)

        val comment = commentList[0]

        val newCommentContent = "새로운 기본 댓글"
        comment.updateComment(
            UpdateCommentDto(content = newCommentContent)
        )

        commentList = commentRepository.findAll()
        assertThat(commentList).isNotEmpty
        assertThat(commentList.size).isEqualTo(1)
        assertThat(commentList[0].content).isNotEqualTo(commentContent)
        assertThat(commentList[0].content).isEqualTo(newCommentContent)
        assertThat(commentList[0]).isEqualTo(comment)
    }

    @AfterEach
    fun cleanUp() {
        commentRepository.deleteAll()
        articleRepository.deleteAll()
        articleTypeRepository.deleteAll()
        memberRepository.deleteAll()
    }
}
