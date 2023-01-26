package pl.edu.pwr.psi.epk.ticket.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.time.Duration

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
    @GeneratedValue(strategy = GenerationType.TABLE)
    open val id: Long = 0

}

@Entity
class OfferedOneWayTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer
): OfferedTicket(price, isReduced, offer)

@Entity
class OfferedTimeLimitedTicket (
    price: Double,
    isReduced: Boolean = false,
    offer: TicketOffer,
    val duration: Duration
): OfferedTicket(price, isReduced, offer)
