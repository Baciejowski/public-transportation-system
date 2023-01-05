package pl.edu.pwr.psi.epk.account.dto

import pl.edu.pwr.psi.epk.account.model.Role
import java.time.LocalDate

data class UserReadDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val dateOfBirth: LocalDate,
    val role: Role,
    val accountBalance: Double
)
