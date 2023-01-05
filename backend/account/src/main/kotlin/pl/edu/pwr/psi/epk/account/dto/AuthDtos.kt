package pl.edu.pwr.psi.epk.account.dto

import pl.edu.pwr.psi.epk.account.model.TokenPair
import java.time.LocalDate
import java.time.LocalDateTime

data class RegisterDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate
)

data class LoginDto(
    val email: String,
    val password: String
)

data class TokenReadDto(
    val accessToken: String,
    val accessTokenExpirationDate: LocalDateTime,
    val refreshToken: String,
    val refreshTokenExpirationDate: LocalDateTime,
    val type: String = "Bearer"
) {
    companion object {
        fun fromTokenPair(tokenPair: TokenPair): TokenReadDto = TokenReadDto(
            tokenPair.accessToken.token,
            tokenPair.accessToken.expiresAt,
            tokenPair.refreshToken.token,
            tokenPair.refreshToken.expiresAt
        )
    }
}
