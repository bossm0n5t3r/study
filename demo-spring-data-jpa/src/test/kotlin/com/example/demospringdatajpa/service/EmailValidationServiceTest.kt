package com.example.demospringdatajpa.service

import com.example.demospringdatajpa.domain.member.Member
import com.example.demospringdatajpa.domain.member.MemberRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class EmailValidationServiceTest {
    @Autowired
    private lateinit var emailValidationService: EmailValidationService

    @Autowired
    private lateinit var memberRepository: MemberRepository

    @Test
    @DisplayName("이메일 형식 테스트")
    fun verifyEmail() {
        val emailT1 = "test@test.com"
        val emailT2 = "test123123@test.com"
        val emailT3 = "test@test.aa.bb"
        val emailT4 = "test@test.aa.bb.cc"
        val emailT5 = "test@test.aa.bb.cc.dd"
        val emailT6 = "test@test.aa.bb.cc.dd.ee"

        val emailF1 = "test @test.com"
        val emailF2 = "test@test .com"
        val emailF3 = "test@test. com"
        val emailF4 = "te st@test.com"
        val emailF5 = "test@test@com"
        val emailF6 = "test123@test#com"

        assertTrue(emailValidationService.verifyEmail(emailT1))
        assertTrue(emailValidationService.verifyEmail(emailT2))
        assertTrue(emailValidationService.verifyEmail(emailT3))
        assertTrue(emailValidationService.verifyEmail(emailT4))
        assertTrue(emailValidationService.verifyEmail(emailT5))
        assertTrue(emailValidationService.verifyEmail(emailT6))

        assertFalse(emailValidationService.verifyEmail(emailF1))
        assertFalse(emailValidationService.verifyEmail(emailF2))
        assertFalse(emailValidationService.verifyEmail(emailF3))
        assertFalse(emailValidationService.verifyEmail(emailF4))
        assertFalse(emailValidationService.verifyEmail(emailF5))
        assertFalse(emailValidationService.verifyEmail(emailF6))
    }

    @Test
    @DisplayName("이미 존재하는 이메일인지 확인합니다.")
    fun isAlreadySignedUpEmail() {
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

        val newEmail = "newTest@test.test"

        assertTrue(emailValidationService.isAlreadySignedUpEmail(email))
        assertFalse(emailValidationService.isAlreadySignedUpEmail(newEmail))
    }
}
