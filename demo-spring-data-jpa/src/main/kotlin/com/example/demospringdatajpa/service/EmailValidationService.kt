package com.example.demospringdatajpa.service

import com.example.demospringdatajpa.domain.member.MemberRepository
import org.springframework.stereotype.Service
import java.util.regex.Pattern

@Service
class EmailValidationService(
    private val memberRepository: MemberRepository
) {
    private val EMAIL_PATTERN = Pattern.compile("""(?:[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#${'$'}%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])""")

    fun verifyEmail(email: String?): Boolean {
        if (email.isNullOrEmpty()) return false
        return EMAIL_PATTERN.matcher(email).matches()
    }

    fun isAlreadySignedUpEmail(email: String?): Boolean {
        if (email.isNullOrEmpty()) return false
        return memberRepository.findByEmail(email) != null
    }
}