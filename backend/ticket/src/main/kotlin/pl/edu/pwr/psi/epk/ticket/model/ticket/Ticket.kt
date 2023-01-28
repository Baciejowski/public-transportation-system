package pl.edu.pwr.psi.epk.ticket.model.ticket

import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Ticket(
    open val passengerId: Long,
    open val pricePaid: Double,
    open val isReduced: Boolean
) {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    open val ticketNo: Long = -1

    open var punchTime: LocalDateTime? = null
}

@Entity
class OneWayTicket(
    passengerId: Long,
    pricePaid: Double,
    reducedPrice: Boolean
): Ticket(passengerId, pricePaid, reducedPrice) {

    var rideId: Long? = null
}

@Entity
class TimeLimitedTicket(
    passengerId: Long,
    pricePaid: Double,
    reducedPrice: Boolean,
    val duration: Duration,
): Ticket(passengerId, pricePaid, reducedPrice)