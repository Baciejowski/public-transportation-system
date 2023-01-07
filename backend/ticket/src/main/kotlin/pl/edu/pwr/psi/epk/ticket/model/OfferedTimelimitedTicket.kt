package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*

@Entity
@Table
class OfferedTimelimitedTicket (
    @ManyToOne
    var offer: TicketOffer,
    var price: Double,
    var minutesValid: Int,
    @Id
    @GeneratedValue
    var id: Int? = null
)