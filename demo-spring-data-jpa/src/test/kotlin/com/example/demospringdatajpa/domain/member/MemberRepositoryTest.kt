package com.example.demospringdatajpa.domain.member

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    @Test
    fun createMember() {
        // given
        val email = "test@test.test"
        val password = "password"
        val name = "name"

        memberRepository.save(
            Member(
                email = email,
                password = password,
                name = name
            )
        )

        // when
        val members = memberRepository.findAll()

        // then
        assertThat(members).isNotEmpty
        assertThat(members.size).isEqualTo(1)
        val member = members[0]
        assertThat(member.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue
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

        val members = memberRepository.findAll()

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
}
