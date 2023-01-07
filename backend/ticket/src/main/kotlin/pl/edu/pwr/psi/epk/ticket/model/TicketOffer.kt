package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.util.Date


@Entity
@Table(name = "TicketOffers")
class TicketOffer (
    var offerStart: Date,
    var offerEnd: Date? = null,
    @OneToMany
    var offeredOnewayTickets: Set<OfferedOnewayTicket> = mutableSetOf(),
    @OneToMany
    var offeredTimelimitedTickets: Set<OfferedTimelimitedTicket> = mutableSetOf(),
    @Id
    @GeneratedValue
    var id: Int? = null
)