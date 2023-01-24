package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.time.Duration

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class OfferedTicket(
    open val price: Double,
    open val isReduced: Boolean = false,

    @ManyToOne
    open val offer: TicketOffer
) {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    open val id: Long = 0

}

@Entity
@Table
class OfferedOneWayTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer
): OfferedTicket(price, isReduced, offer)

@Entity
@Table
class OfferedTimeLimitedTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer,
    val duration: Duration
): OfferedTicket(price, isReduced, offer)
