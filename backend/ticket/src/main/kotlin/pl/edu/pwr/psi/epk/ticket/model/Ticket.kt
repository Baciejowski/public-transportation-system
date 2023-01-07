package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.util.*


@Entity
@Table(name = "Tickets")
@Inheritance(strategy = InheritanceType.JOINED)
abstract class Ticket(
    var passengerEmail: String,
    //TODO
    var pricePaid: Double,
    var reducedPrice: Boolean,
    var punchedTime: Date? = null,
    @Id
    @GeneratedValue
    var ticketNo: Long? = null
)