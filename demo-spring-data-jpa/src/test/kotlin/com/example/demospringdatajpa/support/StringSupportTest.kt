package com.example.demospringdatajpa.support

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class StringSupportTest {
    private val stringSupport = StringSupport()

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

        assertTrue(stringSupport.verifyEmail(emailT1))
        assertTrue(stringSupport.verifyEmail(emailT2))
        assertTrue(stringSupport.verifyEmail(emailT3))
        assertTrue(stringSupport.verifyEmail(emailT4))
        assertTrue(stringSupport.verifyEmail(emailT5))
        assertTrue(stringSupport.verifyEmail(emailT6))

        assertFalse(stringSupport.verifyEmail(emailF1))
        assertFalse(stringSupport.verifyEmail(emailF2))
        assertFalse(stringSupport.verifyEmail(emailF3))
        assertFalse(stringSupport.verifyEmail(emailF4))
        assertFalse(stringSupport.verifyEmail(emailF5))
        assertFalse(stringSupport.verifyEmail(emailF6))
    }
}
