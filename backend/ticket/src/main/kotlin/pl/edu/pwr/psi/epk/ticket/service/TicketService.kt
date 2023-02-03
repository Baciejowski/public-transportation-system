package pl.edu.pwr.psi.epk.ticket.service

import feign.FeignException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.edu.pwr.psi.epk.ticket.client.AccountApiService
import pl.edu.pwr.psi.epk.ticket.exception.ApiCallException
import pl.edu.pwr.psi.epk.ticket.model.ticket.OneWayTicket
import pl.edu.pwr.psi.epk.ticket.model.ticket.Ticket
import pl.edu.pwr.psi.epk.ticket.model.ticket.TicketFactory
import pl.edu.pwr.psi.epk.ticket.model.ticket.TimeLimitedTicket
import pl.edu.pwr.psi.epk.ticket.repository.OfferedTicketRepository
import pl.edu.pwr.psi.epk.ticket.repository.TicketRepository
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class TicketService(
    val offeredTicketRepository: OfferedTicketRepository,
    val ticketRepository: TicketRepository,
    val accountApiService: AccountApiService
) {

    fun getCurrentTickets(passengerId: Long): List<Ticket> {
        val dateFrom = LocalDateTime.now(ZoneOffset.UTC).minusDays(2)
        return ticketRepository.findAllCurrentTickets(passengerId, dateFrom)
    }

    @Transactional
    fun buyTickets(passengerId: Long, offeredTicketId: Long, quantity: Int): List<Ticket> {
        if (quantity < 1)
            throw IllegalArgumentException("Quantity must be at least 1.")

        val offeredTicket = offeredTicketRepository.findById(offeredTicketId).get()
        if (!offeredTicket.offer.isCurrentlyValid())
            throw IllegalArgumentException("Offer is not valid.")

        val tickets = (1..quantity).map { TicketFactory.createTicket(offeredTicket, passengerId) }
        val savedTickets: List<Ticket> = ticketRepository.saveAll(tickets)

        val amountToPay = offeredTicket.price.toBigDecimal().multiply(quantity.toBigDecimal()).toDouble()

        try {
            accountApiService.deduceBalance(passengerId, amountToPay)
        } catch (ex: FeignException) {
            when (ex.status()) {
                HttpStatus.BAD_REQUEST.value() ->
                    throw IllegalArgumentException("Account service returned: ${ex.message}")
                else -> throw ApiCallException("Error when trying to deduce balance using api service. ${ex.message}")
            }
        } catch (ex: Exception) {
            throw ApiCallException("Unexpected error when trying to deduce balance using api service. $ex")
        }

        return savedTickets
    }

    fun punchTicket(passengerId: Long, ticketId: Long, rideId: Long): Ticket {

        val ticket: Ticket = ticketRepository.findById(ticketId).get()

        if (ticket.passengerId != passengerId)
            throw IllegalArgumentException("Ticket does not belong to the passenger")
        if (ticket.isPunched())
            throw IllegalArgumentException("Ticket is already punched.")

        when (ticket) {
            is OneWayTicket -> ticket.punch(rideId)
            is TimeLimitedTicket -> ticket.punch()
        }

        return ticketRepository.save(ticket)
    }

    fun validateTicket(ticketId: Long, rideId: Long): Boolean {

        val ticket: Ticket = ticketRepository.findById(ticketId).get()

        if (!ticket.isPunched())
            throw IllegalArgumentException("Ticket is not punched.")

        return when (ticket) {
            is OneWayTicket -> rideId == ticket.rideId
            is TimeLimitedTicket -> LocalDateTime.now(ZoneOffset.UTC)
                .isBefore(ticket.punchTime!!.plus(ticket.duration))
            else -> throw IllegalArgumentException("Ticket type is not supported.")
        }
    }
}
