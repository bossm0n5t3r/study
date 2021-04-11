package com.example.demospringdatajpa.domain.member

import com.example.demospringdatajpa.domain.BaseEntity
import com.google.common.hash.Hashing
import org.apache.commons.codec.binary.Hex
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "member_info")
class Member() : BaseEntity<Int>() {
    @Column
    lateinit var email: String
        protected set

    @Column
    lateinit var password: String
        protected set

    @Column
    lateinit var name: String
        protected set

    constructor(email: String, password: String, name: String) : this() {
        this.email = email
        this.password = encryptPassword(email, password, name)
        this.name = name
    }

    private fun encryptPassword(email: String, password: String, name: String): String {
        require(password.isNotEmpty())
        return Hex.encodeHexString(Hashing.sha256().hashString("$email$password$name", Charsets.UTF_8).asBytes())
    }

    fun verifyPassword(otherPassword: String): Boolean {
        return this.password == encryptPassword(this.email, otherPassword, this.name)
    }
}
