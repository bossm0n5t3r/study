package com.example.demospringdatajpa.domain.board

import com.example.demospringdatajpa.domain.board.type.BoardType
import com.example.demospringdatajpa.domain.board.type.BoardTypeRepository
import com.example.demospringdatajpa.domain.member.Member
import com.example.demospringdatajpa.domain.member.MemberRepository
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

    private fun createMemberAndBoardType(): Pair<Member, BoardType> {
        // member
        val email = "test@test.test"
        val password = "password"
        val name = "name"

        // boardType
        val boardTypeName = "공지"

        memberRepository.save(
            Member(
                email = email,
                password = password,
                name = name
            )
        )
        boardTypeRepository.save(
            BoardType(name = boardTypeName)
        )

        val member = memberRepository.findAll().first()
        val boardType = boardTypeRepository.findAll().first()

        return Pair(member, boardType)
    }

    @Test
    fun findAll() {
        // given

        // board
        val subject = "테스트 제목"
        val content = "테스트 본문"

        val (member, boardType) = createMemberAndBoardType()

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

    /**
     * TODO 추가 테스트 목록
     *  업데이트
     *      1. 제목 업데이트
     *      2. 본문 업데이트
     *      3. board type 업데이트
     *      4. hits 클릭 시 업데이트
     *      5. recommend 추천 버튼 누를 시 업데이트
     *  삭제
     *      6. 게시물 삭제
     *  조회
     *      7. 회원별 게시물 리스트 조회
     *      8. board type 별 게시물 리스트 조회
     *      9. 조회수별 게시물 리스트 조회
     *      10. 추천별 게시물 리스트 조회
     */

    @Test
    fun updateSubject() {
        // given

        // board
        val subject = "테스트 제목"
        val content = "테스트 본문"

        val (member, boardType) = createMemberAndBoardType()

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
        board.updateBoard(
            UpdateBoardDto(subject = newSubject)
        )

        // then
        val boardId = board.id!!
        val newBoard = boardRepositorySupport.findById(boardId)
        assertThat(newBoard).isNotNull
        assertThat(newBoard!!.subject).isEqualTo(newSubject)
        assertThat(newBoard).isEqualTo(board)
    }

    @AfterEach
    fun cleanUp() {
        boardRepository.deleteAll()
        memberRepository.deleteAll()
        boardTypeRepository.deleteAll()
    }
}
