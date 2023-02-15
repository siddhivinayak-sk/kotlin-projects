package com.sk.project7.repos

import com.sk.project7.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.util.Optional

@Repository
interface UserRepository: JpaRepository<User, Long> {
    fun findByName(name: String?): Optional<User>
}