package pl.edu.pwr.psi.epk.integration.dto.offer

import java.time.Duration
import java.time.LocalDateTime

data class TicketDto (
    val ticketNo: Long,
    val passengerId: Long,
    val pricePaid: Double,
    val isReduced: Boolean,
    val punchTime: LocalDateTime?,
    val duration: Duration?,
    val rideId: Long?
)