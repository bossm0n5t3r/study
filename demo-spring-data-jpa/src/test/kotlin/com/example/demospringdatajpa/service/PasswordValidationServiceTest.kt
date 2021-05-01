package com.example.demospringdatajpa.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class PasswordValidationServiceTest {
    private val passwordValidationService = PasswordValidationService()

    @Test
    @DisplayName("비밀번호 길이 체크 : 8 ~ 20 길이여야 한다.")
    fun checkPasswordLength() {
        val passwordLength7 = "abc1234"

        val passwordLength8 = "abcd1234"
        val passwordLength20 = "abcdefghij0123456789"

        val passwordLength21 = "abcdefghij01234567890"

        assertThat(passwordValidationService.checkPassword(passwordLength7)).isEqualTo(PasswordStrength.WRONG_PATTERN)

        assertThat(passwordValidationService.checkPassword(passwordLength8)).isNotEqualTo(PasswordStrength.WRONG_PATTERN)
        assertThat(passwordValidationService.checkPassword(passwordLength20)).isNotEqualTo(PasswordStrength.WRONG_PATTERN)

        assertThat(passwordValidationService.checkPassword(passwordLength21)).isEqualTo(PasswordStrength.WRONG_PATTERN)
    }

    @Test
    @DisplayName("비밀번호 영문 + 숫자 조합 체크 : 8 ~ 20 길이")
    fun checkEnglishAndNumberAreExist() {
        val passwordOnlyEnglish = "onlyEnglish"
        val passwordOnlyNumber = "0123456789"
        val passwordOnlyLowerCaseEnglish = "onlylowercaseenglish"
        val passwordOnlyUpperCaseEnglish = "ONLYUPPERCASEENGLISH"

        val passwordMixedAll = "English0123456789"
        val passwordWithSpecialCharacters = "password_!0123456789"

        assertThat(passwordValidationService.checkPassword(passwordOnlyEnglish)).isEqualTo(PasswordStrength.WRONG_PATTERN)
        assertThat(passwordValidationService.checkPassword(passwordOnlyNumber)).isEqualTo(PasswordStrength.WRONG_PATTERN)
        assertThat(passwordValidationService.checkPassword(passwordOnlyLowerCaseEnglish)).isEqualTo(PasswordStrength.WRONG_PATTERN)
        assertThat(passwordValidationService.checkPassword(passwordOnlyUpperCaseEnglish)).isEqualTo(PasswordStrength.WRONG_PATTERN)

        assertThat(passwordValidationService.checkPassword(passwordMixedAll)).isNotEqualTo(PasswordStrength.WRONG_PATTERN)
        assertThat(passwordValidationService.checkPassword(passwordWithSpecialCharacters)).isNotEqualTo(PasswordStrength.WRONG_PATTERN)
    }
}
