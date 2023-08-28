package com.example.simpleblog.config

import com.example.simpleblog.domain.member.Member
import com.example.simpleblog.domain.member.MemberRepository
import com.example.simpleblog.domain.member.Role
import io.github.serpro69.kfaker.Faker
import io.github.serpro69.kfaker.fakerConfig
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.annotation.Configuration
import org.springframework.context.event.EventListener

@Configuration
class InitData(
    private val memberRepository: MemberRepository
) {
    private final val config = fakerConfig { locale = "ko" }
    val faker = Faker(config)

    @EventListener(ApplicationReadyEvent::class)
    private fun init() {
        val member = Member(
            email = faker.internet.safeEmail()
            , password = "plainpassword"
            , role = Role.USER
        )
        memberRepository.save(member)
    }

}