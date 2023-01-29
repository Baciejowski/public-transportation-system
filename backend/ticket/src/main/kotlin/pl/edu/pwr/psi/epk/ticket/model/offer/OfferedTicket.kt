package pl.edu.pwr.psi.epk.ticket.model.offer

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.Duration
import java.time.LocalDateTime

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
abstract class OfferedTicket(

    open val price: Double,

    open val isReduced: Boolean = false,

    @JsonIgnore
    @ManyToOne
    open val offer: TicketOffer
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open val id: Long = 0

}

@Entity
class OfferedOneWayTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer
): OfferedTicket(price, isReduced, offer) {

    constructor(): this(0.0, false, TicketOffer())

}

@Entity
class OfferedTimeLimitedTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer,
    val duration: Duration): OfferedTicket(price, isReduced, offer) {

    constructor(): this(0.0, false, TicketOffer(), Duration.ZERO)

}
