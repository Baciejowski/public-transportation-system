package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class Ticket(
    open val passengerId: Long,
    open val pricePaid: Double,
    open val reducedPrice: Boolean
) {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    open val ticketNo: Long = 0

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
