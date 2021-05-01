package com.example.demospringdatajpa.service

import com.nulabinc.zxcvbn.Zxcvbn
import java.util.regex.Pattern

class PasswordValidationService {
    private val PASSWORD_PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-zA-Z]).{8,20}$")

    fun checkPassword(password: String): PasswordStrength {
        if (!PASSWORD_PATTERN.matcher(password).matches()) return PasswordStrength.WRONG_PATTERN
        val passwordStrengthChecker = Zxcvbn()
        val passwordStrength = passwordStrengthChecker.measure(password)
        val passwordScore = passwordStrength.score
        return PasswordStrength.values().first { it.score == passwordScore }
    }
}

enum class PasswordStrength(val score: Int) {
    WRONG_PATTERN(-1),
    WEAK(0),
    FAIR(1),
    GOOD(2),
    STRONG(3),
    VERY_STRONG(4),
}
