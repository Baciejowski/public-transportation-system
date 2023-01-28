package pl.edu.pwr.psi.epk.ticket.model.ticket

import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedOneWayTicket
import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedTicket
import pl.edu.pwr.psi.epk.ticket.model.offer.OfferedTimeLimitedTicket

class TicketFactory {

    companion object {
        fun createTicket(offeredTicket: OfferedTicket, passengerId: Long): Ticket {
            when (offeredTicket) {
                is OfferedOneWayTicket -> {
                    return OneWayTicket(
                        passengerId,
                        offeredTicket.price,
                        offeredTicket.isReduced
                    )
                }
                is OfferedTimeLimitedTicket -> {
                    return TimeLimitedTicket(
                        passengerId,
                        offeredTicket.price,
                        offeredTicket.isReduced,
                        offeredTicket.duration
                    )
                }
                else -> { throw IllegalArgumentException("There is no such ticket type.") }
            }
        }
    }

}