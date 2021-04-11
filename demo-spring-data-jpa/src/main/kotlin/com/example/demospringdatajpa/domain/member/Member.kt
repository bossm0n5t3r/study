package com.example.demospringdatajpa.domain.member

import com.example.demospringdatajpa.domain.BaseEntity
import com.google.common.hash.Hashing
import org.apache.commons.codec.binary.Hex
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member_info")
class Member(
    email: String,
    password: String,
    name: String
) : BaseEntity<Int>() {
    @Column
    var email: String = email

    @Column
    var password: String = encryptPassword(email, password, name)

    @Column
    var name: String = name

    private fun encryptPassword(email: String, password: String, name: String): String {
        require(password.isNotEmpty())
        return Hex.encodeHexString(Hashing.sha256().hashString("$email$password$name", Charsets.UTF_8).asBytes())
    }

    fun setPassword(newPassword: String): Member {
        this.password = encryptPassword(this.email, newPassword, this.name)
        return this
    }

    fun verifyPassword(otherPassword: String): Boolean {
        return this.password == encryptPassword(this.email, otherPassword, this.name)
    }
}
