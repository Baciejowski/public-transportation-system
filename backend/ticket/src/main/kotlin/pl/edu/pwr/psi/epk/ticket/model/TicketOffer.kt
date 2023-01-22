package pl.edu.pwr.psi.epk.ticket.model

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
class TicketOffer (
    val offerStart: LocalDateTime,
    val offerEnd: LocalDateTime? = null
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0

    @OneToMany(mappedBy = "offer")
    val tickets = mutableSetOf<OfferedTicket>()

}
