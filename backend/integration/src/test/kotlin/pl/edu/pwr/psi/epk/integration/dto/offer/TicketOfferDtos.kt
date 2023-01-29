package pl.edu.pwr.psi.epk.integration.dto.offer

import java.time.Duration
import java.time.LocalDateTime

class TicketOfferDto (
    val id: Long,
    val offerStart: LocalDateTime,
    val offerEnd: LocalDateTime?,
    val tickets: Set<OfferedSingleTicketDto>
)

abstract class OfferedTicket(
    open val id: Long,
    open val price: Double,
    open val isReduced: Boolean = false
)

class OfferedSingleTicketDto(
    id: Long,
    price: Double,
    isReduced: Boolean = false
) : OfferedTicket(id, price, isReduced)

class OfferedTimeLimitedTicketDto(
    id: Long,
    price: Double,
    isReduced: Boolean = false,
    val duration: Duration
) : OfferedTicket(id, price, isReduced)
