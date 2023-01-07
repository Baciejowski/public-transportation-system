package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "TimelimitedTickets")
class TimelimitedTicket(
    passengerEmail: String,
    pricePaid: Double,
    reducedPrice: Boolean,
    var minutesValid: Int,
    punchedTime: Date? = null,
): Ticket(passengerEmail, pricePaid, reducedPrice, punchedTime)