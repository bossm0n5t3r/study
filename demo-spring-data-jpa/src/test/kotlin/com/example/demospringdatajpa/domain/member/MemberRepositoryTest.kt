package com.example.demospringdatajpa.domain.member

import com.querydsl.core.types.Predicate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    lateinit var memberRepository: MemberRepository

    private val qMember: QMember = QMember.member

    @Test
    fun `JpaRepository - findAll`() {
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
        val members = memberRepository.findAll()

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
    fun `JpaRepository - findByEmail`() {
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
        val member = memberRepository.findByEmail(email)

        // then
        assertThat(member).isNotNull
        assertThat(member!!.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue
    }

    @Test
    fun `JpaRepository - findByName`() {
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
        val member = memberRepository.findByName(name)

        // then
        assertThat(member).isNotNull
        assertThat(member!!.email).isEqualTo(email)
        assertThat(member.name).isEqualTo(name)
        assertThat(member.password).isNotEqualTo(password)
        assertThat(member.verifyPassword(password)).isTrue
    }

    @Test
    fun `QuerydslPredicateExecutor - findOne`() {
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
        val predicate: Predicate = qMember.name.eq(newName)
        val result = memberRepository.findOne(predicate)

        // then
        assertThat(result.isPresent).isTrue
        assertThat(result.get().email).isEqualTo(newEmail)
        assertThat(result.get().name).isEqualTo(newName)
        assertThat(result.get().password).isNotEqualTo(newPassword)
        assertThat(result.get().verifyPassword(newPassword)).isTrue
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

    @AfterEach
    fun cleanUp() {
        memberRepository.deleteAll()
    }
}
