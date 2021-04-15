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

    @Test
    fun `QuerydslRepositorySupport - findAll`() {
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

        val newEmail = "new@new.new"
        val newPassword = "new_password"
        val newName = "new_name"

        memberRepository.save(
            Member(
                email = newEmail,
                password = newPassword,
                name = newName
            )
        )

        // when
        val members = memberRepositorySupport.findAll()

        // then
        assertThat(members).isNotEmpty
        assertThat(members.size).isEqualTo(2)

        // member
        val member = members.first()

        assertThat(member.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue

        // new member
        val newMember = members.last()

        assertThat(newMember.email).isEqualTo(newEmail)
        assertThat(newMember.name).isEqualTo(newName)
        assertThat(newMember.password).isNotEqualTo(newPassword)
        assertThat(newMember.verifyPassword(newPassword)).isTrue
    }

    @Test
    fun `QuerydslRepositorySupport - findByEmail`() {
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

        val newEmail = "new@new.new"
        val newPassword = "new_password"
        val newName = "new_name"

        memberRepository.save(
            Member(
                email = newEmail,
                password = newPassword,
                name = newName
            )
        )

        // when
        val member = memberRepositorySupport.findByEmail(email)

        // then
        assertThat(member).isNotNull
        assertThat(member!!.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue
    }

    @Test
    fun `QuerydslRepositorySupport - findByName`() {
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

        val newEmail = "new@new.new"
        val newPassword = "new_password"
        val newName = "new_name"

        memberRepository.save(
            Member(
                email = newEmail,
                password = newPassword,
                name = newName
            )
        )

        // when
        val member = memberRepositorySupport.findByName(name)

        // then
        assertThat(member).isNotNull
        assertThat(member!!.email).isEqualTo(email)
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
