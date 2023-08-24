package com.example.simpleblog.domain.member

import jakarta.persistence.*

@Entity
@Table(name = "Member")
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long,
    @Column(name = "email", nullable = false) var email: String,
    @Column(name = "password") var password: String,
    @Enumerated(EnumType.STRING) var role: Role
) {}

enum class Role {
    USER, ADMIN
}