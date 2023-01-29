package pl.edu.pwr.psi.epk.ticket.model.offer

import jakarta.persistence.*
import java.time.LocalDateTime
import java.time.ZoneOffset

@Entity
class TicketOffer (
    val offerStart: LocalDateTime,
    val offerEnd: LocalDateTime? = null
) {

    constructor(): this(LocalDateTime.MIN, null)

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = -1

    @OneToMany(mappedBy = "offer", cascade = [CascadeType.ALL])
    val tickets = mutableSetOf<OfferedTicket>()

    fun isCurrentlyValid(): Boolean {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        return offerStart.isBefore(now) && (offerEnd?.isAfter(now) ?: true)
    }
}
