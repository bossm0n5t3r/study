package com.example.demospringdatajpa.domain.member

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberRepositorySupportTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Autowired
    lateinit var memberRepositorySupport: MemberRepositorySupport

    private val email = "test@test.test"
    private val password = "password"
    private val name = "name"

    private fun createMember() {
        memberRepository.save(
            Member(
                email = email,
                password = password,
                name = name
            )
        )
    }

    private fun verifyMember(member: Member?) {
        assertThat(member).isNotNull
        assertThat(member!!.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue
    }

    @Test
    fun findAll() {
        // given
        createMember()

        // when
        val members = memberRepositorySupport.findAll()

        // then
        assertThat(members).isNotEmpty
        assertThat(members.size).isEqualTo(1)
        val member = members[0]

        verifyMember(member)
    }

    @Test
    fun findByEmail() {
        // given
        createMember()

        // when
        val member = memberRepositorySupport.findByEmail(email)

        // then
        verifyMember(member)
    }

    @Test
    fun findByName() {
        // given
        createMember()

        // when
        val member = memberRepositorySupport.findByName(name)

        // then
        verifyMember(member)
    }

    @Test
    fun updatePassword() {
        // given
        val email = "test@test.test"
        val oldPassword = "password"
        val name = "name"

        memberRepository.save(
            Member(
                email = email,
                password = oldPassword,
                name = name
            )
        )

        val members = memberRepositorySupport.findAll()

        assertThat(members).isNotEmpty
        assertThat(members.size).isEqualTo(1)

        val member = members[0]
        assertThat(member.verifyPassword(oldPassword)).isTrue

        // when
        val newPassword = "new_password"
        member.setPassword(newPassword)

        // then
        assertThat(member.verifyPassword(oldPassword)).isFalse
        assertThat(member.verifyPassword(newPassword)).isTrue
    }

    @AfterEach
    fun cleanUp() {
        memberRepository.deleteAll()
    }
}
