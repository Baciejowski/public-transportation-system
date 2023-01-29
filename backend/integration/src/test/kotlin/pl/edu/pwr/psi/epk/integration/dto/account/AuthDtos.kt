package pl.edu.pwr.psi.epk.integration.dto.account

import java.time.LocalDateTime

data class RegisterDto(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
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
)
