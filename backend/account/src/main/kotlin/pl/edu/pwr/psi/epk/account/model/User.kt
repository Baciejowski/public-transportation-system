package pl.edu.pwr.psi.epk.account.model

import jakarta.persistence.*

enum class Role {
    PASSENGER, TICKET_INSPECTOR, MODERATOR, PLANNER
}
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class User(
    open val email: String,
    open val password: String,
    open val firstName: String,
    open val lastName: String
    ) {

    constructor(): this("", "", "", "")

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    open val id: Long = 0

    open var active: Boolean = true

    abstract val role: Role

}
@Entity
class Passenger(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
) : User(email, password, firstName, lastName) {

    constructor(): this("", "", "", "")

    var walletBalance: Double = 0.0

    override val role: Role = Role.PASSENGER

}

@Entity
class TicketInspector(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
) : User(email, password, firstName, lastName) {

    constructor(): this("", "", "", "")

    override val role: Role = Role.TICKET_INSPECTOR

}

@Entity
class Moderator(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
) : User(email, password, firstName, lastName) {

    constructor(): this("", "", "", "")

    override val role: Role = Role.MODERATOR

}

@Entity
class Planner(
    email: String,
    password: String,
    firstName: String,
    lastName: String,
) : User(email, password, firstName, lastName) {

    constructor(): this("", "", "", "")

    override val role: Role = Role.PLANNER

}
