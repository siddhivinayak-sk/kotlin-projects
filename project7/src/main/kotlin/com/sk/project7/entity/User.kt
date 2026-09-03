package com.sk.project7.entity

import java.security.Principal
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "u_user")
class User: Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    @Column(name = "u_password")
    var password: String = ""

    @Column(name = "u_name")
    private var name: String = ""

    @Column(name = "u_role")
    var role: String = ""

    override fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }
}