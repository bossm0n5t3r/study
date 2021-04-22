package com.example.demospringdatajpa.domain.article

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
class ArticleRepositoryTest {
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var articleTypeRepository: ArticleTypeRepository
    @Autowired
    lateinit var articleRepository: ArticleRepository

    private val subject = "테스트 제목"
    private val content = "테스트 본문"

    private fun createMemberListAndArticleType(
        numberOfMember: Int = 1,
        numberOfArticleType: Int = 1
    ): Pair<List<Member>, List<ArticleType>> {
        // member
        val email = "test@test.test"
        val password = "password"
        val name = "name"

        for (i in 1..numberOfMember) {
            memberRepository.save(
                Member(
                    email = "$i$email",
                    password = "$i$password",
                    name = "$i$name"
                )
            )
        }

        // articleType
        val articleTypeName = "공지"

        for (i in 1..numberOfArticleType) {
            articleTypeRepository.save(
                ArticleType(name = "$i$articleTypeName")
            )
        }

        val memberList = memberRepository.findAll().toList()
        val articleType = articleTypeRepository.findAll().toList()

        return Pair(memberList, articleType)
    }

    @Test
    fun findAll() {
        // given
        val numberOfMember = 1
        val (memberList, articleTypeList) = createMemberListAndArticleType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val articleType = articleTypeList.first()

        articleRepository.save(
            Article(
                subject = subject,
                content = content,
                member = member,
                articleType = articleType
            )
        )

        // when
        val articleList: List<Article> = articleRepository.findAll()

        // then
        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(1)

        val article = articleList[0]
        assertThat(article.subject).isEqualTo(subject)
        assertThat(article.content).isEqualTo(content)
        assertThat(article.member).isEqualTo(member)
        assertThat(article.articleType).isEqualTo(articleType)
        assertThat(article.hits).isEqualTo(0)
        assertThat(article.recommend).isEqualTo(0)
    }

    @Test
    fun updateArticle() {
        // given
        val numberOfMember = 1
        val (memberList, articleTypeList) = createMemberListAndArticleType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val articleType = articleTypeList.first()

        articleRepository.save(
            Article(
                subject = subject,
                content = content,
                member = member,
                articleType = articleType
            )
        )

        val articleList = articleRepository.findAll()
        assertThat(articleList).isNotEmpty
        val article = articleList.first()
        assertThat(article.subject).isEqualTo(subject)

        // when
        val newSubject = "새로운 테스트 제목"
        val newContent = "새로운 테스트 본문"
        val newArticleTypeName = "새로운 공지"
        articleTypeRepository.save(
            ArticleType(name = newArticleTypeName)
        )
        val newArticleType = articleTypeRepository.findByName(newArticleTypeName)
        article.updateArticle(
            UpdateArticleDto(
                subject = newSubject,
                content = newContent,
                articleType = newArticleType
            )
        )

        // then
        val articleId = article.id!!
        val optionalNewArticle = articleRepository.findById(articleId)
        assertThat(optionalNewArticle.isPresent).isTrue

        val newArticle = optionalNewArticle.get()
        assertThat(newArticle.subject).isEqualTo(newSubject)
        assertThat(newArticle.content).isEqualTo(newContent)
        assertThat(newArticle.articleType).isEqualTo(newArticleType)
        assertThat(newArticle).isEqualTo(article)
    }

    @Test
    fun deleteArticle() {
        // given
        val numberOfMember = 1
        val (memberList, articleTypeList) = createMemberListAndArticleType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val articleType = articleTypeList.first()

        articleRepository.save(
            Article(
                subject = subject,
                content = content,
                member = member,
                articleType = articleType
            )
        )

        val newSubject = "새로운 테스트 제목"
        val newContent = "새로운 테스트 본문"
        articleRepository.save(
            Article(
                subject = newSubject,
                content = newContent,
                member = member,
                articleType = articleType
            )
        )

        var articleList = articleRepository.findAll()
        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(2)
        val article = articleList.first()
        val otherArticle = articleList.last()

        // when
        articleRepository.delete(article)

        // then
        articleList = articleRepository.findAll()
        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(1)
        assertThat(articleList.first()).isEqualTo(otherArticle)
    }

    @Test
    fun `회원별 게시물 리스트 조회`() {
        val numberOfMember = 3
        val (memberList, articleTypeList) = createMemberListAndArticleType(
            numberOfMember = numberOfMember
        )
        val articleType = articleTypeList.first()

        memberList.forEach {
            // 아래 index 가 실제 회원의 article 라는 것을 입증하는 키가 됩니다.
            val index = it.email[0].toString()
            articleRepository.save(
                Article(
                    subject = "$index$subject",
                    content = "$index$content",
                    member = it,
                    articleType = articleType
                )
            )
        }

        val firstMember = memberList.first()
        val articleList: List<Article> = articleRepository.findByMember(firstMember)

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(1)

        // checkingIndex 는 회원의 email 의 첫번째 char 를 통해서 비교하면 됩니다.
        val checkingIndex = firstMember.email[0]
        assertThat(articleList[0].subject[0]).isEqualTo(checkingIndex)
        assertThat(articleList[0].content[0]).isEqualTo(checkingIndex)
    }

    @Test
    fun `article type 별 게시물 리스트 조회`() {
        val numberOfArticleType = 3
        val (memberList, articleTypeList) = createMemberListAndArticleType(
            numberOfArticleType = numberOfArticleType
        )

        val member = memberList.first()

        articleTypeList.forEach {
            val index = it.name[0].toString()
            articleRepository.save(
                Article(
                    subject = "$index$subject",
                    content = "$index$content",
                    member = member,
                    articleType = it
                )
            )
        }

        val firstArticleType = articleTypeList.first()
        val articleList: List<Article> = articleRepository.findByArticleType(firstArticleType)

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(1)

        // checkingIndex 는 articleType 의 name 의 첫번째 char 를 통해서 비교하면 됩니다.
        val checkingIndex = firstArticleType.name[0]
        assertThat(articleList[0].subject[0]).isEqualTo(checkingIndex)
        assertThat(articleList[0].content[0]).isEqualTo(checkingIndex)
    }

    @Test
    fun `조회수 내림차순 게시물 리스트 조회`() {
        val (memberList, articleTypeList) = createMemberListAndArticleType()

        val member = memberList.first()
        val articleType = articleTypeList.first()

        val startHits = 1
        val endHits = 9

        for (i in startHits..endHits) {
            articleRepository.save(
                Article(
                    subject = "$i$subject",
                    content = "$i$content",
                    member = member,
                    articleType = articleType,
                    hits = i
                )
            )
        }

        var articleList: List<Article> = articleRepository.findAllByOrderByHitsAsc()

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(9)
        assertThat(articleList.first().subject[0].toString()).isEqualTo(startHits.toString())
        assertThat(articleList.first().content[0].toString()).isEqualTo(startHits.toString())

        articleList = articleRepository.findAllByOrderByHitsDesc()

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(9)
        assertThat(articleList.first().subject[0].toString()).isEqualTo(endHits.toString())
        assertThat(articleList.first().content[0].toString()).isEqualTo(endHits.toString())
    }

    @Test
    fun `추천별 게시물 리스트 조회`() {
        val (memberList, articleTypeList) = createMemberListAndArticleType()

        val member = memberList.first()
        val articleType = articleTypeList.first()

        val startRecommend = 1
        val endRecommend = 9

        for (i in startRecommend..endRecommend) {
            articleRepository.save(
                Article(
                    subject = "$i$subject",
                    content = "$i$content",
                    member = member,
                    articleType = articleType,
                    recommend = i
                )
            )
        }

        var articleList: List<Article> = articleRepository.findAllByOrderByRecommendAsc()

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(9)
        assertThat(articleList.first().subject[0].toString()).isEqualTo(startRecommend.toString())
        assertThat(articleList.first().content[0].toString()).isEqualTo(startRecommend.toString())

        articleList = articleRepository.findAllByOrderByRecommendDesc()

        assertThat(articleList).isNotEmpty
        assertThat(articleList.size).isEqualTo(9)
        assertThat(articleList.first().subject[0].toString()).isEqualTo(endRecommend.toString())
        assertThat(articleList.first().content[0].toString()).isEqualTo(endRecommend.toString())
    }

    @AfterEach
    fun cleanUp() {
        articleRepository.deleteAll()
        memberRepository.deleteAll()
        articleTypeRepository.deleteAll()
    }
}
