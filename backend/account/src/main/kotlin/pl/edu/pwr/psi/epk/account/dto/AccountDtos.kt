package pl.edu.pwr.psi.epk.account.dto

import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.Role
import pl.edu.pwr.psi.epk.account.model.User

open class UserReadDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Role,
) {

    companion object {
        fun fromUser(user: User): UserReadDto =
            when (user) {
                is Passenger -> PassengerReadDto(
                    user.id,
                    user.email,
                    user.firstName,
                    user.lastName,
                    user.role,
                    user.walletBalance
                )
                else -> UserReadDto(
                    user.id,
                    user.email,
                    user.firstName,
                    user.lastName,
                    user.role
                )
            }
    }
}

class PassengerReadDto(
    id: Long,
    email: String,
    firstName: String,
    lastName: String,
    role: Role,
    val accountBalance: Double
): UserReadDto(id, email, firstName, lastName, role)

class BalanceTopUpDto(
    val amount: Double
)
