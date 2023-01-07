package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "OnewayTickets")
class OnewayTicket(
    passengerEmail: String,
    pricePaid: Double,
    reducedPrice: Boolean,
    punchedTime: Date? = null,
    var serviceId: Int? = null,
    var busId: Int? = null,
    var serviceDate: Date? = null
): Ticket(passengerEmail, pricePaid, reducedPrice, punchedTime)