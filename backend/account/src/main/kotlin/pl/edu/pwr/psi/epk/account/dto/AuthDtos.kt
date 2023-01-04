package pl.edu.pwr.psi.epk.account.dto

import pl.edu.pwr.psi.epk.account.model.Role
import java.time.LocalDateTime

data class RegisterDto(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val dateOfBirth: LocalDateTime
)

data class LoginDto(
        val email: String,
        val password: String
)

data class UserReadDto(
        val id: Long,
        val email: String,
        val firstName: String,
        val lastName: String,
        val dateOfBirth: LocalDateTime,
        val role: Role,
        val accountBalance: Double
)

data class TokenReadDto(
        val accessToken: String,
        val accessTokenExpirationDate: LocalDateTime,
        val refreshToken: String,
        val refreshTokenExpirationDate: LocalDateTime,
        val type: String = "Bearer"
)
