package pl.edu.pwr.psi.epk.account.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*


class Token(val token: String, val expiresAt: LocalDateTime)
class TokenPair(val accessToken: Token, val refreshToken: Token)

@Entity
class TokenFamily {

    @Id
    var id: String = UUID.randomUUID().toString()

    @Column(length = 5000, nullable = false)
    var validToken: String? = null

    var isInvalidated: Boolean = false

}
