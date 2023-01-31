package pl.edu.pwr.psi.epk.integration.dto.account

open class UserReadDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Role,
)

class PassengerReadDto(
    id: Long,
    email: String,
    firstName: String,
    lastName: String,
    role: Role,
    val accountBalance: Double
): UserReadDto(id, email, firstName, lastName, role)

enum class Role {
    PASSENGER, TICKET_INSPECTOR, MODERATOR, PLANNER
}

class BalanceTopUpDto(
    val amount: Double
)
