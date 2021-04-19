package com.example.demospringdatajpa.domain.board

import com.example.demospringdatajpa.domain.board.type.BoardType
import com.example.demospringdatajpa.domain.board.type.BoardTypeRepository
import com.example.demospringdatajpa.domain.member.Member
import com.example.demospringdatajpa.domain.member.MemberRepository
import com.querydsl.core.types.Order
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
@Transactional
class BoardRepositorySupportTest {
    @Autowired
    lateinit var memberRepository: MemberRepository
    @Autowired
    lateinit var boardTypeRepository: BoardTypeRepository
    @Autowired
    lateinit var boardRepository: BoardRepository
    @Autowired
    lateinit var boardRepositorySupport: BoardRepositorySupport
    @PersistenceContext
    lateinit var entityManager: EntityManager

    private val subject = "테스트 제목"
    private val content = "테스트 본문"

    private fun createMemberListAndBoardType(
        numberOfMember: Int = 1,
        numberOfBoardType: Int = 1
    ): Pair<List<Member>, List<BoardType>> {
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

        // boardType
        val boardTypeName = "공지"

        for (i in 1..numberOfBoardType) {
            boardTypeRepository.save(
                BoardType(name = "$i$boardTypeName")
            )
        }

        val memberList = memberRepository.findAll().toList()
        val boardType = boardTypeRepository.findAll().toList()

        return Pair(memberList, boardType)
    }

    @Test
    fun findAll() {
        // given
        val numberOfMember = 1
        val (memberList, boardTypeList) = createMemberListAndBoardType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val boardType = boardTypeList.first()

        boardRepository.save(
            Board(
                subject = subject,
                content = content,
                member = member,
                boardType = boardType
            )
        )

        // when
        val boardList: List<Board> = boardRepositorySupport.findAll()

        // then
        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(1)

        val board = boardList[0]
        assertThat(board.subject).isEqualTo(subject)
        assertThat(board.content).isEqualTo(content)
        assertThat(board.member).isEqualTo(member)
        assertThat(board.boardType).isEqualTo(boardType)
        assertThat(board.hits).isEqualTo(0)
        assertThat(board.recommend).isEqualTo(0)
    }

    @Test
    fun updateBoard() {
        // given
        val numberOfMember = 1
        val (memberList, boardTypeList) = createMemberListAndBoardType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val boardType = boardTypeList.first()

        boardRepository.save(
            Board(
                subject = subject,
                content = content,
                member = member,
                boardType = boardType
            )
        )

        val boardList = boardRepositorySupport.findAll()
        assertThat(boardList).isNotEmpty
        val board = boardList.first()
        assertThat(board.subject).isEqualTo(subject)

        // when
        val newSubject = "새로운 테스트 제목"
        val newContent = "새로운 테스트 본문"
        val newBoardTypeName = "새로운 공지"
        boardTypeRepository.save(
            BoardType(name = newBoardTypeName)
        )
        val newBoardType = boardTypeRepository.findByName(newBoardTypeName)
        board.updateBoard(
            UpdateBoardDto(
                subject = newSubject,
                content = newContent,
                boardType = newBoardType
            )
        )

        // then
        val boardId = board.id!!
        val newBoard = boardRepositorySupport.findById(boardId)
        assertThat(newBoard).isNotNull
        assertThat(newBoard!!.subject).isEqualTo(newSubject)
        assertThat(newBoard.content).isEqualTo(newContent)
        assertThat(newBoard.boardType).isEqualTo(newBoardType)
        assertThat(newBoard).isEqualTo(board)
    }

    @Test
    fun deleteBoard() {
        // given
        val numberOfMember = 1
        val (memberList, boardTypeList) = createMemberListAndBoardType(
            numberOfMember = numberOfMember
        )
        val member = memberList.first()
        val boardType = boardTypeList.first()

        boardRepository.save(
            Board(
                subject = subject,
                content = content,
                member = member,
                boardType = boardType
            )
        )

        val newSubject = "새로운 테스트 제목"
        val newContent = "새로운 테스트 본문"
        boardRepository.save(
            Board(
                subject = newSubject,
                content = newContent,
                member = member,
                boardType = boardType
            )
        )

        var boardList = boardRepositorySupport.findAll()
        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(2)
        val board = boardList.first()
        val otherBoard = boardList.last()

        // when
        boardRepository.delete(board)

        // then
        boardList = boardRepositorySupport.findAll()
        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(1)
        assertThat(boardList.first()).isEqualTo(otherBoard)
    }

    @Test
    fun `회원별 게시물 리스트 조회`() {
        val numberOfMember = 3
        val (memberList, boardTypeList) = createMemberListAndBoardType(
            numberOfMember = numberOfMember
        )
        val boardType = boardTypeList.first()

        memberList.forEach {
            // 아래 index 가 실제 회원의 board 라는 것을 입증하는 키가 됩니다.
            val index = it.email[0].toString()
            boardRepository.save(
                Board(
                    subject = "$index$subject",
                    content = "$index$content",
                    member = it,
                    boardType = boardType
                )
            )
        }

        val firstMember = memberList.first()
        val boardList: List<Board> = boardRepositorySupport.findByMember(firstMember)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(1)

        // checkingIndex 는 회원의 email 의 첫번째 char 를 통해서 비교하면 됩니다.
        val checkingIndex = firstMember.email[0]
        assertThat(boardList[0].subject[0]).isEqualTo(checkingIndex)
        assertThat(boardList[0].content[0]).isEqualTo(checkingIndex)
    }

    @Test
    fun `board type 별 게시물 리스트 조회`() {
        val numberOfBoardType = 3
        val (memberList, boardTypeList) = createMemberListAndBoardType(
            numberOfBoardType = numberOfBoardType
        )

        val member = memberList.first()

        boardTypeList.forEach {
            val index = it.name[0].toString()
            boardRepository.save(
                Board(
                    subject = "$index$subject",
                    content = "$index$content",
                    member = member,
                    boardType = it
                )
            )
        }

        val firstBoardType = boardTypeList.first()
        val boardList: List<Board> = boardRepositorySupport.findByBoardType(firstBoardType)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(1)

        // checkingIndex 는 boardType 의 name 의 첫번째 char 를 통해서 비교하면 됩니다.
        val checkingIndex = firstBoardType.name[0]
        assertThat(boardList[0].subject[0]).isEqualTo(checkingIndex)
        assertThat(boardList[0].content[0]).isEqualTo(checkingIndex)
    }

    @Test
    fun `조회수 내림차순 게시물 리스트 조회`() {
        val (memberList, boardTypeList) = createMemberListAndBoardType()

        val member = memberList.first()
        val boardType = boardTypeList.first()

        val startHits = 1
        val endHits = 9

        for (i in startHits..endHits) {
            boardRepository.save(
                Board(
                    subject = "$i$subject",
                    content = "$i$content",
                    member = member,
                    boardType = boardType,
                    hits = i
                )
            )
        }

        var boardList: List<Board> = boardRepositorySupport.orderByHits(Order.ASC)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(9)
        assertThat(boardList.first().subject[0].toString()).isEqualTo(startHits.toString())
        assertThat(boardList.first().content[0].toString()).isEqualTo(startHits.toString())

        boardList = boardRepositorySupport.orderByHits(Order.DESC)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(9)
        assertThat(boardList.first().subject[0].toString()).isEqualTo(endHits.toString())
        assertThat(boardList.first().content[0].toString()).isEqualTo(endHits.toString())
    }

    @Test
    fun `추천별 게시물 리스트 조회`() {
        val (memberList, boardTypeList) = createMemberListAndBoardType()

        val member = memberList.first()
        val boardType = boardTypeList.first()

        val startRecommend = 1
        val endRecommend = 9

        for (i in startRecommend..endRecommend) {
            boardRepository.save(
                Board(
                    subject = "$i$subject",
                    content = "$i$content",
                    member = member,
                    boardType = boardType,
                    recommend = i
                )
            )
        }

        var boardList: List<Board> = boardRepositorySupport.orderByRecommend(Order.ASC)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(9)
        assertThat(boardList.first().subject[0].toString()).isEqualTo(startRecommend.toString())
        assertThat(boardList.first().content[0].toString()).isEqualTo(startRecommend.toString())

        boardList = boardRepositorySupport.orderByRecommend(Order.DESC)

        assertThat(boardList).isNotEmpty
        assertThat(boardList.size).isEqualTo(9)
        assertThat(boardList.first().subject[0].toString()).isEqualTo(endRecommend.toString())
        assertThat(boardList.first().content[0].toString()).isEqualTo(endRecommend.toString())
    }

    @AfterEach
    fun cleanUp() {
        boardRepository.deleteAll()
        memberRepository.deleteAll()
        boardTypeRepository.deleteAll()
    }
}
