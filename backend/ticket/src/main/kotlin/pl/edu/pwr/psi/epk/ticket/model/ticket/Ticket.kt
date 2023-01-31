package pl.edu.pwr.psi.epk.ticket.model.ticket

import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class Ticket(
    open val passengerId: Long,
    open val pricePaid: Double,
    open val isReduced: Boolean
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val ticketNo: Long = -1

    open var punchTime: LocalDateTime? = null

    fun isPunched() = punchTime != null

    open fun punch() {
        punchTime = LocalDateTime.now(ZoneOffset.UTC)
    }
}

@Entity
class OneWayTicket(
    passengerId: Long,
    pricePaid: Double,
    isReduced: Boolean
): Ticket(passengerId, pricePaid, isReduced) {

    constructor() : this(-1, 0.0, false)

    var rideId: Long? = null

    fun punch(rideId: Long) {
        super.punch()
        this.rideId = rideId
    }
}

@Entity
class TimeLimitedTicket(
    passengerId: Long,
    pricePaid: Double,
    isReduced: Boolean,
    val duration: Duration,
): Ticket(passengerId, pricePaid, isReduced) {

    constructor() : this(-1, 0.0, false, Duration.ZERO)

}
