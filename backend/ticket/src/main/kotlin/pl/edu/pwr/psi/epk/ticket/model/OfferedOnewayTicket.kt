package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*

@Entity
@Table
class OfferedOnewayTicket (
    @ManyToOne
    var offer: TicketOffer,
    var price: Double,
    @Id
    @GeneratedValue
    var id: Int? = null
)