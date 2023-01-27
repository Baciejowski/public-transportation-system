package pl.edu.pwr.psi.epk.account.dto

import com.fasterxml.jackson.annotation.JsonInclude
import pl.edu.pwr.psi.epk.account.model.Passenger
import pl.edu.pwr.psi.epk.account.model.Role
import pl.edu.pwr.psi.epk.account.model.User

data class UserReadDto(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val role: Role,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val accountBalance: Double?
) {

    companion object {
        fun fromUser(user: User): UserReadDto = UserReadDto(
            user.id,
            user.email,
            user.firstName,
            user.lastName,
            user.role,
            kotlin.runCatching { (user as Passenger).walletBalance }.getOrNull()
        )

    }
}
