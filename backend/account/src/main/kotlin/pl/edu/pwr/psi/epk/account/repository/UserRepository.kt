package pl.edu.pwr.psi.epk.account.repository

import org.springframework.data.jpa.repository.JpaRepository
import pl.edu.pwr.psi.epk.account.model.User

interface UserRepository: JpaRepository<User, Long> {

    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean

}